package com.example.ieaadmin;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    NotificationManager mNotificationManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(false);
        }

        // vibration
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 300, 300, 300};
        v.vibrate(pattern, -1);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "ieaadmin");

        Intent resultIntent = null;

        if(Objects.requireNonNull(remoteMessage.getData().get("activity")).matches("grievance")){
            resultIntent = new Intent(this,GrievanceDetail.class).putExtra("GrievanceItemKey",remoteMessage.getData().get("chatKey"));
        } else if(Objects.requireNonNull(remoteMessage.getData().get("activity")).matches("memberapproval")){
            resultIntent = new Intent(this,memberApprovalDetail.class).putExtra("memberApprovalKey",remoteMessage.getData().get("ownerKey"));
        }else if(Objects.requireNonNull(remoteMessage.getData().get("activity")).matches("memberrenewal")){
            resultIntent = new Intent(this,MembershipRenewal.class);
        } else{
            resultIntent = new Intent(this, MembersNotification.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "ieaadmin";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Notifications",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            builder.setSmallIcon(R.drawable.notification_icon);
            builder.setChannelId(channelId);
            builder.setContentIntent(pendingIntent);
            builder.setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle());
            builder.setContentText(remoteMessage.getNotification().getBody());
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_MAX);
        } else{
            builder.setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle());
            builder.setContentText(remoteMessage.getNotification().getBody());
            builder.setChannelId("ieaadmin");
            builder.setSmallIcon(R.drawable.notification_icon);
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setContentIntent(pendingIntent);

        }

        mNotificationManager.notify(0, builder.build());

    }

}


