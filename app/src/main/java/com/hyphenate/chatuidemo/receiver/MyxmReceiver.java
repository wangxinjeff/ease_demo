package com.hyphenate.chatuidemo.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.chatuidemo.R;
import com.hyphenate.push.platform.mi.EMMiMsgReceiver;
import com.hyphenate.util.EMLog;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MyxmReceiver extends EMMiMsgReceiver {

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        super.onReceiveRegisterResult(context, message);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        super.onNotificationMessageArrived(context, message);
//        NotificationManager mNotificationManager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("title").setContentText("text").setSmallIcon(R.drawable.em_logo_uidemo);
        Notification notification = builder.build();
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        try {
            Intent msgIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            msgIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            String from;
            String content = message.getContent();
            JSONObject obj = new JSONObject(content);
            if(obj.has("g")){
                from = obj.getString("g");
                msgIntent.putExtra("chattype",2);
            }else {
                from = obj.getString("f");
                msgIntent.putExtra("chattype",1);
            }

            msgIntent.putExtra("from", from);
            context.startActivity(msgIntent);
        }catch(JSONException e){
            e.printStackTrace();
        }

    }

}
