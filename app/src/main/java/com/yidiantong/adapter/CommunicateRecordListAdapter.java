package com.yidiantong.adapter;

import android.content.Context;
import android.icu.util.TimeUnit;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.yidiantong.R;
import com.yidiantong.bean.FollowRecordsBean;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.TimeUtils;
import com.yidiantong.widget.AudioPlayerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommunicateRecordListAdapter extends RecyclerView.Adapter<CommunicateRecordListAdapter.ViewHolder> {

    private List<FollowRecordsBean> communicateRecordList = new ArrayList<>();
    private Context mContext;
    private AudioPlayerView playingView;
    private int clickPosition = -1;

    public CommunicateRecordListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setCommunicateRecordList(List<FollowRecordsBean> communicateRecordList) {
        this.communicateRecordList = communicateRecordList;
        notifyDataSetChanged();
    }

    public void stopAudio() {
        if (playingView != null) {
            playingView.pause();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_xrv_communicate_record_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FollowRecordsBean bean = communicateRecordList.get(position);
        holder.tvOperator.setText(bean.getTitle());
        holder.tvCallType.setText(bean.getType());
        holder.tvTime.setText(bean.getTime());
        if (StringUtils.isNullOrBlank(bean.getRemark())) {
            holder.tvRemark.setVisibility(View.GONE);
        } else {
            holder.tvRemark.setVisibility(View.VISIBLE);
            holder.tvRemark.setText(bean.getRemark());
        }

        if (StringUtils.isNullOrBlank(bean.getSoundRecordingUrl())) {
            holder.aplv_sound.setVisibility(View.GONE);
        } else {
            holder.aplv_sound.setVisibility(View.VISIBLE);
            if (StringUtils.isNullOrBlank(holder.aplv_sound.getText().toString())) {
                holder.aplv_sound.setText(TimeUtils.getDuration(bean.getCallDuration() * 1000));
            }
            holder.aplv_sound.setUrlCallBack(new UrlCallBack() {
                @Override
                public void setUrlCallBack() {
                    holder.aplv_sound.setUrl(bean.getSoundRecordingUrl());
                }

                @Override
                public void setOnClicked() {
                    if (clickPosition != position) {
                        clickPosition = position;
                        stopAudio();
                        playingView = holder.aplv_sound;
                    }
                }
            });
        }

        // 防止被挡住的底部padding
        if (position >= communicateRecordList.size() - 1) {
            holder.footerPadding.setVisibility(View.VISIBLE);
        } else {
            holder.footerPadding.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return communicateRecordList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_operator)
        TextView tvOperator;
        @BindView(R.id.tv_remark)
        TextView tvRemark;
        @BindView(R.id.tv_call_type)
        TextView tvCallType;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.aplv_sound)
        AudioPlayerView aplv_sound;
        @BindView(R.id.footer_padding)
        View footerPadding;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface UrlCallBack {
        void setUrlCallBack();

        void setOnClicked();
    }
}
