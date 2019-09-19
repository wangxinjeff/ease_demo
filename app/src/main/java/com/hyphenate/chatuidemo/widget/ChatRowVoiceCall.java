package com.hyphenate.chatuidemo.widget;

import android.content.Context;
import android.graphics.Color;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;

public class ChatRowVoiceCall extends EaseChatRow{

    private TextView contentvView;
    private Button state;

    public ChatRowVoiceCall(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    R.layout.ease_row_received_voice_call : R.layout.ease_row_sent_voice_call, this);
        // video call
        }else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    R.layout.ease_row_received_video_call : R.layout.ease_row_sent_video_call, this);
        }
    }

    @Override
    protected void onFindViewById() {
        contentvView = (TextView) findViewById(R.id.tv_chatcontent);
        state = findViewById(R.id.state);
    }

    @Override
    protected void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        contentvView.setText(txtBody.getMessage());
        boolean accept = message.getBooleanAttribute("accept", false);
        boolean think = message.getBooleanAttribute("think", false);
        if(accept){
            state.setBackgroundColor(Color.RED);
            state.setText("accept");
            state.setTextColor(Color.WHITE);
        }else if(think){
            state.setBackgroundColor(Color.BLUE);
            state.setText("think");
            state.setTextColor(Color.BLACK);
        }


    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
    }
}
