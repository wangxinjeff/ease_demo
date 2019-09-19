/*
 *  * EaseMob CONFIDENTIAL
 * __________________
 * Copyright (C) 2017 EaseMob Technologies. All rights reserved.
 *
 * NOTICE: All information contained herein is, and remains
 * the property of EaseMob Technologies.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from EaseMob Technologies.
 */
package com.hyphenate.chatuidemo.receiver;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.support.api.push.PushReceiver;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.util.EMLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HMSPushReceiver extends PushReceiver{
    private static final String TAG = HMSPushReceiver.class.getSimpleName();

    @Override
    public void onToken(Context context, String token, Bundle extras){
        if(token != null && !token.equals("")){
            //没有失败回调，假定token失败时token为null
            EMLog.d("HWHMSPush", "register huawei hms push token success token:" + token);
            EMClient.getInstance().sendHMSPushTokenToServer(token);
        }else{
            EMLog.e("HWHMSPush", "register huawei hms push token fail!");
        }
    }


    public void onEvent(Context context, Event event, Bundle extras) {
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            Log.i(TAG, "收到通知栏消息点击事件,notifyId:" + notifyId);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
        }
        try {
//            Intent msgIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
//            msgIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//

            String from;
            int chattype;
            String message = extras.getString(BOUND_KEY.pushMsgKey);
            JSONArray jsonArray = new JSONArray(message);

            EMLog.e("HWHMSPush", "extras:" + extras + "==pushmessage:" + jsonArray.toString());
            JSONObject obj = jsonArray.getJSONObject(0);
            EMLog.e("HWHMSPush", "extras:" + extras + "==pushmessage:" + obj.toString());
            if(obj.has("g")){
                from = obj.getString("g");
                chattype = 2;
//                msgIntent.putExtra("chattype",2);
            }else {
                from = obj.getString("f");
                chattype = 1;
//                msgIntent.putExtra("chattype",1);
            }
//
//            msgIntent.putExtra("from", from);
//
            EMLog.e("HWHMSPush", "==intent:" + from+"//"+chattype);
//            context.startActivity(msgIntent);
            Intent intent = new Intent(context, ChatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constant.EXTRA_CHAT_TYPE, chattype);
            // it's single chat
            intent.putExtra(Constant.EXTRA_USER_ID, from);


            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
