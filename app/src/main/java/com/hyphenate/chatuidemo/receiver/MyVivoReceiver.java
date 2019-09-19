package com.hyphenate.chatuidemo.receiver;

import android.content.Context;

import com.hyphenate.push.platform.vivo.EMVivoMsgReceiver;
import com.hyphenate.util.EMLog;
import com.vivo.push.model.UPSNotificationMessage;

public class MyVivoReceiver extends EMVivoMsgReceiver {
    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage upsNotificationMessage) {
        super.onNotificationMessageClicked(context, upsNotificationMessage);
        EMLog.e("MyVivoReceiver", "onNotificationMessageClicked");

    }
}
