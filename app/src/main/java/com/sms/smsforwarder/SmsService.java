package com.sms.smsforwarder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

public class SmsService extends Service {

    private final String TAG = this.getClass().getSimpleName();

    private MessageReceiver mSMSreceiver;
    private IntentFilter mIntentFilter;
    private String CHANNEL_ID = "SmsService";

    @Override
    public void onCreate() {
        super.onCreate();


        Log.i(TAG, "Communicator started");
        //SMS event receiver
        mSMSreceiver = new MessageReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        mIntentFilter.setPriority(2147483647);
        registerReceiver(mSMSreceiver, mIntentFilter);

        Intent intent = new Intent("android.provider.Telephony.SMS_RECEIVED");
        List<ResolveInfo> infos = getPackageManager().queryBroadcastReceivers(intent, 0);
        for (ResolveInfo info : infos) {
            Log.i(TAG, "Receiver name:" + info.activityInfo.name + "; priority=" + info.priority);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder notificationBuilder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createNotificationChannel();
            notificationBuilder = new Notification.Builder(this, CHANNEL_ID);

        } else {
            notificationBuilder = new Notification.Builder(this);
        }



        Notification notification= notificationBuilder.setContentTitle("SMS Forwarder")
                .setContentText("SMS service is running").build();


        startForeground(111,notification);
        return  Service.START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"SMS SERVICE", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager=getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSMSreceiver);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


}