package com.yidiantong.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contrarywind.view.WheelView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.yidiantong.R;
import com.yidiantong.app.MyLinPhoneManager;
import com.yidiantong.bean.CluesListBean;
import com.yidiantong.bean.TalkTimeInfoBean;
import com.yidiantong.bean.request.CallRecordsDto;
import com.yidiantong.model.biz.IMain;
import com.yidiantong.model.impl.MainImpl;
import com.yidiantong.presenter.MainPresenter;
import com.yidiantong.presenter.home.CallingPresenter;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.SysUtil;
import com.yidiantong.util.TimeFormat;
import com.yidiantong.util.ToastUtils;
import com.yidiantong.view.home.CallingActivity;
import com.yidiantong.view.home.CluesDetailActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.ViewHolder> {

    private List<CluesListBean> cluesList = new ArrayList<>();
    private Context mContext;
    private String industry = "";
    private TalkTimeInfoBean talkTimeInfoBean;
    private IMain iMain;

    public CallListAdapter(Context mContext, IMain iMain) {
        this.mContext = mContext;
        this.iMain = iMain;
    }

    public void setCluesList(List<CluesListBean> cluesList) {
        this.cluesList = cluesList;
        notifyDataSetChanged();
    }

    public List<CluesListBean> getCluesList() {
        return this.cluesList;
    }

    public void setTalkTimeInfoBean(TalkTimeInfoBean talkTimeInfoBean) {
        this.talkTimeInfoBean = talkTimeInfoBean;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_xrv_clues_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CluesListBean bean = cluesList.get(position);
        // 212 3456 7890
//        if(bean.getNumber().length()==11){
        if (!StringUtils.isEmpty(bean.getNumber())) {
            if (StringUtils.isPhoneNumberType(bean.getNumber())) {
                String phoneNum = bean.getNumber().substring(0, 3) + " " + bean.getNumber().substring(3, 7) + " " + bean.getNumber().substring(7);
                holder.tvCallPhoneNum.setText(phoneNum);
            } else {
                holder.tvCallPhoneNum.setText(bean.getNumber());
            }
        }
   /*     if (StringUtils.isPhoneNumberType(bean.getNumber())) {
            String phoneNum = bean.getNumber().substring(0, 3) + " " + bean.getNumber().substring(3, 7) + " " + bean.getNumber().substring(7);

            holder.tvCallPhoneNum.setText(phoneNum);

            }else {
            holder.tvCallPhoneNum.setText(bean.getNumber());
         }*/
        // 时间地址和状态显示如果为空，则隐藏布局
        if (StringUtils.isNullOrBlank(bean.getCallTime()) && StringUtils.isNullOrBlank(bean.getCallTime())) {
            holder.tvTimeNAddress.setText("");
            holder.tvCallState.setText("");
            holder.llTimeAddressState.setVisibility(View.GONE);
        } else {
            holder.tvTimeNAddress.setText(bean.getCallTime());
            holder.tvCallState.setText(bean.getStatus());
            holder.llTimeAddressState.setVisibility(View.VISIBLE);
        }

        // item点击事件
        holder.rlCluesListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iMain.hideKeyboard(false);
                Intent intent = new Intent(mContext, CluesDetailActivity.class);
                intent.putExtra("cluesId", bean.getId());
                intent.putExtra("industry", industry);
                intent.putExtra("area", bean.getNumberLocation());
                intent.putExtra("cluesList", (Serializable) cluesList);
                intent.putExtra("callPosition", position);
                ((Activity) mContext).startActivityForResult(intent, MainPresenter.REQUEST_CODE_CLUES_UPDATE);
            }
        });

        // 点击事件
        holder.ivCallImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iMain.hideKeyboard(false);
//                showCallDialog("拨打运维人员电话", Constants.callPhoneNum, "拨打", cluesList.get(position).getNumber());
                callPhone(position, bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cluesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_call_img)
        ImageView ivCallImg;
        @BindView(R.id.tv_call_phone_num)
        TextView tvCallPhoneNum;
        @BindView(R.id.tv_time_n_address)
        TextView tvTimeNAddress;
        @BindView(R.id.tv_call_state)
        TextView tvCallState;
        @BindView(R.id.rl_clues_list_item)
        RelativeLayout rlCluesListItem;
        @BindView(R.id.ll_time_address_state)
        LinearLayout llTimeAddressState;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    // 拨打
    public void callPhone(int position, CluesListBean bean) {
        // 2020-11-28 去掉直拨电话
//        if (StringUtils.isNullOrBlank(bean.getPhoneNumber())) {
        if (talkTimeInfoBean != null && !StringUtils.isNullOrBlank(talkTimeInfoBean.getTrafficSurplus())) {
            double time = Double.parseDouble(talkTimeInfoBean.getTrafficSurplus());
            if (time > 0) {
                Intent intent = new Intent(mContext, CallingActivity.class);
                if (!StringUtils.isNullOrBlank(bean.getNumber())) {
                    intent.putExtra("phoneNum", bean.getNumber());
                } else if (!StringUtils.isNullOrBlank(bean.getPhoneNumber())) {
                    intent.putExtra("phoneNum", bean.getPhoneNumber());
                } else {
                    intent.putExtra("phoneNum", "");
                }
                intent.putExtra("cluesList", (Serializable) cluesList);
                intent.putExtra("callPosition", position);
                intent.putExtra("cluesId", bean.getId());
                intent.putExtra("industry", industry);
                intent.putExtra("area", bean.getNumberLocation());
                intent.putExtra("isInputCall", false);
                ((Activity) mContext).startActivityForResult(intent, MainPresenter.REQUEST_CODE_CLUES_UPDATE);
                bean.setCalled(true);
            } else {
                ToastUtils.showToast(mContext, "话务为0不可拨打");
            }
        } else {
            ToastUtils.showToast(mContext, "话务时间有误");
        }
//        } else {
//            showCallDialog(position, "拨打线索电话", bean.getPhoneNumber(), "拨打", bean.getId());
//        }
    }

    /**
     * 弹框
     *
     * @param title
     * @param content
     * @param confirm
     */
    public void showCallDialog(int position, String title, String content, String confirm, String cluesId) {
        new XPopup.Builder(mContext)
//                        .hasBlurBg(true)
                .dismissOnTouchOutside(false)
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
//                        .isLightStatusBar(true)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onCreated() {
                        Log.e("tag", "弹窗创建了");
                    }

                    @Override
                    public void onShow() {
                        Log.e("tag", "onShow");
                    }

                    @Override
                    public void onDismiss() {
                        Log.e("tag", "onDismiss");
                    }

                    //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
                    @Override
                    public boolean onBackPressed() {
                        return true;
                    }
                }).asConfirm(title, content, "取消", confirm, new OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (onCallClickListener != null) {
                    CallRecordsDto callRecordsDto = new CallRecordsDto();
                    callRecordsDto.setCluesId(cluesId);
                    callRecordsDto.setCallType("普通电话");
                    callRecordsDto.setPhoneNumber(content);
                    callRecordsDto.setCallTime(TimeFormat.dateToStringYyyyMMddHHmmss(System.currentTimeMillis()));
                    onCallClickListener.onCallClick(position, callRecordsDto);
                }
                SysUtil.callPhoneImmediately(mContext, content);
                cluesList.get(position).setCalled(true);
            }
        }, null, false).show();
    }

    /**
     * item 点击处理事件
     */
    public OnCallClickListener onCallClickListener;

    public interface OnCallClickListener {
        void onCallClick(int position, CallRecordsDto callRecordsDto);
    }

    public void setOnCallClickListener(OnCallClickListener onCallClickListener) {
        this.onCallClickListener = onCallClickListener;
    }
}
