package com.yidiantong.adapter;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yidiantong.R;
import com.yidiantong.bean.KeyboardBean;
import com.yidiantong.bean.request.CallRecordsDto;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KeyboardAdapter extends RecyclerView.Adapter<KeyboardAdapter.ViewHolder> {
    private Context mContext;
    private List<KeyboardBean> list = new ArrayList<>();

    public KeyboardAdapter(Context mContext, List<KeyboardBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_keyboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KeyboardBean bean = list.get(position);

        // 显示按钮
        holder.tvInputNumber.setText(bean.getKeyText());
        holder.tvInputHint.setText(bean.getHintText());

        // 点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onGridItemClickListener != null){
                    onGridItemClickListener.onGridItemClick(position, bean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_input_number)
        TextView tvInputNumber;
        @BindView(R.id.tv_input_hint)
        TextView tvInputHint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    /**
     * item 点击处理事件
     */
    public OnGridItemClickListener onGridItemClickListener;

    public interface OnGridItemClickListener {
        void onGridItemClick(int position, KeyboardBean keyboardBean);
    }

    public void setOnGridItemClickListener(OnGridItemClickListener onGridItemClickListener) {
        this.onGridItemClickListener = onGridItemClickListener;
    }
}
