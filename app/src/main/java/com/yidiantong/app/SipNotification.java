package com.yidiantong.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.yidiantong.R;
import com.yzx.yzxsip.service.PhoneService;


public class SipNotification {
    /**
     * 创建服务通知
     */
    public static Notification createForegroundNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // 唯一的通知通道的id.
        String notificationChannelId = "notification_channel_id_01";

        // Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //用户可见的通道名称
            String channelName = "Foreground Service Notification";
            //通道的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, importance);
            notificationChannel.setDescription("Channel description");
            //LED灯
            //notificationChannel.enableLights(false);
            //notificationChannel.setLightColor(Color.RED);
            //震动
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            //notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationChannelId);
        //通知小图标
        builder.setSmallIcon(R.drawable.ic_logo);
        //通知标题
        builder.setContentTitle(context.getResources().getString(R.string.app_name));
        //通知内容
        builder.setContentText(context.getResources().getString(R.string.app_name) + "正在运行");
        //设定通知显示的时间
        builder.setWhen(System.currentTimeMillis());
        //设定启动的内容
        Intent activityIntent = new Intent(context, PhoneService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0x01, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        //创建通知并返回
        return builder.build();
    }
}
