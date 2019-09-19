package com.hyphenate.chatuidemo.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.R;
import com.hyphenate.chatuidemo.conference.ConferenceActivity;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.EasyUtils;

/**
 * 开屏页
 *
 */
public class SplashActivity extends BaseActivity {

	private static final int sleepTime = 2000;
	private String userid;
	private int chattype;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.em_activity_splash);
		super.onCreate(arg0);

//		NotificationManager nm
//				=(NotificationManager)SplashActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
//		nm.cancelAll();
//
//		try {
//			Bundle bunlde = new Bundle();
//			bunlde.putString("package", "com.hyphenate.chatuidemo");
//			bunlde.putString("class", "com.hyphenate.chatuidemo.ui.SplashActivity");
//			bunlde.putInt("badgenumber", 0);
//			SplashActivity.this.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bunlde);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}


//		Intent intent = getIntent();
//		if(intent!= null && intent.getExtras()!=null){
//			EMLog.e("HWHMSPush", intent.getExtras().toString());
//			chattype = intent.getIntExtra("chattype", 0);
//			userid = intent.getStringExtra("from");
//		}

		DemoHelper.getInstance().initHandler(this.getMainLooper());

		RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
		TextView versionText = (TextView) findViewById(R.id.tv_version);

		versionText.setText(getVersion());
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		rootLayout.startAnimation(animation);
	}

	@Override
	protected void onStart() {
		super.onStart();
		new Thread(new Runnable() {
			public void run() {
				if (DemoHelper.getInstance().isLoggedIn()) {
					// auto login mode, make sure all group and conversation is loaed before enter the main screen
					long start = System.currentTimeMillis();
					EMClient.getInstance().chatManager().loadAllConversations();
					EMClient.getInstance().groupManager().loadAllGroups();
					long costTime = System.currentTimeMillis() - start;
					//wait
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					EMLog.e("HWHMSPush", "1111");

					String topActivityName = EasyUtils.getTopActivityName(EMClient.getInstance().getContext());
					if (topActivityName != null && (topActivityName.equals(VideoCallActivity.class.getName()) || topActivityName.equals(VoiceCallActivity.class.getName()) || topActivityName.equals(ConferenceActivity.class.getName()))) {
						// nop
						// avoid main screen overlap Calling Activity
					} else {
//						if(userid != null && !userid.isEmpty()){
//							EMLog.e("HWHMSPush", "222");
//							// start chat acitivity
//							Intent intent = new Intent(SplashActivity.this, ChatActivity.class);
//									intent.putExtra(Constant.EXTRA_CHAT_TYPE, chattype);
//							// it's single chat
//							intent.putExtra(Constant.EXTRA_USER_ID, userid);
//							startActivity(intent);
//						}else {
							EMLog.e("HWHMSPush", "333");
							//enter main screen
							startActivity(new Intent(SplashActivity.this, MainActivity.class));
//						}
					}
					finish();
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					startActivity(new Intent(SplashActivity.this, LoginActivity.class));
					finish();
				}
			}
		}).start();

	}
	
	/**
	 * get sdk version
	 */
	private String getVersion() {
	    return EMClient.getInstance().VERSION;
	}
}
