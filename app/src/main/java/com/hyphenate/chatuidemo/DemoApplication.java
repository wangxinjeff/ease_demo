/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.chatuidemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.hyphenate.easeui.EaseUI;
import com.hyphenate.push.EMPushConfig;
import com.hyphenate.push.EMPushHelper;
import com.hyphenate.push.EMPushType;
import com.hyphenate.push.PushListener;
import com.hyphenate.util.EMLog;

public class DemoApplication extends Application {

	public static Context applicationContext;
	private static DemoApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";

	private static String TAG = DemoApplication.class.getSimpleName();

	/**
	 * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
	 */
	public static String currentUserNick = "";

	@Override
	public void onCreate() {
		MultiDex.install(this);
		super.onCreate();
        applicationContext = this;
        instance = this;
		//umeng推送
//		UMConfigure.init(applicationContext, "5d8047650cafb2520a00086c",
//				"Umeng", UMConfigure.DEVICE_TYPE_PHONE,
//				"77d663e8616c6b44df115777ec8346be");
//
//		//获取消息推送代理示例
//		PushAgent mPushAgent = PushAgent.getInstance(this);
//		//注册推送服务，每次调用register方法都会回调该接口
//		mPushAgent.register(new IUmengRegisterCallback() {
//			@Override
//			public void onSuccess(String deviceToken) {
//				//注册成功会返回deviceToken deviceToken是推送消息的唯一标志
//				EMLog.i("UMENGPUSH","注册成功：deviceToken：-------->  " + deviceToken);
//			}
//			@Override
//			public void onFailure(String s, String s1) {
//				EMLog.e("UMENGPUSH","UMENGPUSH 注册失败：-------->  " + "s:" + s + ",s1:" + s1);
//			}
//		});

		//init demo helper
        DemoHelper.getInstance().init(applicationContext);
        // 请确保环信SDK相关方法运行在主进程，子进程不会初始化环信SDK（该逻辑在EaseUI.java中）
		if (EaseUI.getInstance().isMainProcess(this)) {


			// 初始化华为 HMS 推送服务, 需要在SDK初始化后执行
			HMSPushHelper.getInstance().initHMSAgent(instance);

			EMPushHelper.getInstance().setPushListener(new PushListener() {
				@Override
				public void onError(EMPushType pushType, long errorCode) {
					// TODO: 返回的errorCode仅9xx为环信内部错误，可从EMError中查询，其他错误请根据pushType去相应第三方推送网站查询。
					EMLog.e("PushClient", "Push client occur a error: " + pushType + " - " + errorCode);
				}

				@Override
				public boolean isSupportPush(EMPushType pushType, EMPushConfig pushConfig) {
					EMLog.e("PushClient", pushType.getName()+"::"+pushConfig.toString());
					return super.isSupportPush(pushType, pushConfig);
				}
			});
		}
	}

	public static DemoApplication getInstance() {
		return instance;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
