package com.yidiantong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidiantong.R;
import com.yidiantong.bean.StringCheckedBean;

import java.util.ArrayList;
import java.util.List;

public class BtnCheckedAdapter extends BaseAdapter {


    private List<StringCheckedBean> checkedBeanList = new ArrayList<>();
    private Context mContext;
    private boolean isWhiteBg;
    private int checkedPosition = -1;
    private boolean isRadio;

    public BtnCheckedAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setRadio(boolean isRadio) {
        this.isRadio = isRadio;
    }

    public void setStringCheckedList(List<StringCheckedBean> checkedBeanList, boolean isWhiteBg) {
        this.checkedBeanList = checkedBeanList;
        this.isWhiteBg = isWhiteBg;
        notifyDataSetChanged();
    }

    public void setTagsChoose(List<String> tagsChoose) {
        for (String tag : tagsChoose) {
            for (StringCheckedBean stringCheckedBean : checkedBeanList) {
                if (tag != null && stringCheckedBean.getTitle() != null && tag.equals(stringCheckedBean.getTitle())) {
                    stringCheckedBean.setChecked(true);
                    if (mListener != null) {
                        mListener.onBtnClick(stringCheckedBean.getTitle(), stringCheckedBean.isChecked());
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return checkedBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return checkedBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final StringCheckedBean bean = checkedBeanList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_svgv_checked, null);
            holder.llChecked = (LinearLayout) convertView.findViewById(R.id.ll_checked);
            holder.tvChecked = (TextView) convertView.findViewById(R.id.tv_checked);
            holder.ivEdit = (ImageView) convertView.findViewById(R.id.iv_edit);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // UI显示
        holder.tvChecked.setText(bean.getTitle());
        // 是否为白底黑字
        if (isWhiteBg) {
            if (bean.isChecked()) {
                holder.tvChecked.setTextColor(mContext.getResources().getColor(R.color.blue_3f74fd));
                holder.llChecked.setBackground(mContext.getResources().getDrawable(R.drawable.bg_corner_4dp_white_with_blue_line));
            } else {
                holder.tvChecked.setTextColor(mContext.getResources().getColor(R.color.gray_56575a));
                holder.llChecked.setBackground(mContext.getResources().getDrawable(R.drawable.bg_corner_4dp_white_with_no_line));
            }
        } else {
            holder.tvChecked.setTextColor(mContext.getResources().getColor(R.color.white));
            if (bean.isChecked()) {
                holder.llChecked.setBackground(mContext.getResources().getDrawable(R.drawable.bg_corner_4dp_blue_with_white_line));
            } else {
                holder.llChecked.setBackground(mContext.getResources().getDrawable(R.drawable.bg_corner_4dp_empty_with_white_line));
            }
        }

        // 自定义
        if (bean.getTitle().equals("自定义")) {
            holder.ivEdit.setVisibility(View.VISIBLE);
        } else {
            holder.ivEdit.setVisibility(View.GONE);
        }

        if (bean.isChecked()) {
            checkedPosition = position;
        }
        // 点击事件
        holder.llChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRadio) {
                    if (checkedPosition >= 0) {
                        StringCheckedBean checkedBean = checkedBeanList.get(checkedPosition);
                        checkedBean.setChecked(false);
                    }
                    bean.setChecked(true);
                    checkedPosition = position;
                    notifyDataSetChanged();
                    if (mListener != null) {
                        mListener.onBtnClick(bean.getTitle(), bean.isChecked());
                    }
                } else {
                    bean.setChecked(!bean.isChecked());
                    if (mListener != null) {
                        mListener.onBtnClick(bean.getTitle(), bean.isChecked());
                    }
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        LinearLayout llChecked;
        TextView tvChecked;
        ImageView ivEdit;
    }

    /**
     * item 点击处理事件
     */
    public OnBtnClickListener mListener;

    public interface OnBtnClickListener {
        void onBtnClick(String checkStr, boolean isChecked);
    }

    public void setOnBtnClickListener(OnBtnClickListener mListener) {
        this.mListener = mListener;
    }
}
