package io.github.tadoya.din9talk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.quickstart.fcm.MyFirebaseMessagingService;

public class ChattingActivity extends AppCompatActivity {

    private static final String TAG = "ChattingActivity";
    private Button sendButton;
    private EditText editText;
    private TextView chatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        chatView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        sendButton = (Button)findViewById(R.id.sendButton);

        chatView.setText(MyFirebaseMessagingService.chat);
        chatView.setMovementMethod(new ScrollingMovementMethod());


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GCMSender sender = new GCMSender(editText.getText().toString());
                MyFirebaseMessagingService.chat += "보낸메시지" +" : " +editText.getText().toString()+'\n';
                chatView.setText(MyFirebaseMessagingService.chat);
                editText.setText("");
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 전달된 인텐트를 처리합니다.
        processIntent(intent);
    }
    /**
     * 전달된 인텐트를 처리합니다.
     *
     * @param intent
     */
    private void processIntent(Intent intent) {
        // 수신 내용
        String message = intent.getStringExtra("chat");
        if (message != null) {
            chatView.setText(message);
            chatView.invalidate();
        }
    }

    @Override
    protected void onResume() {
        MyFirebaseMessagingService.isOnChattingActivity = true;
        Log.d(TAG,"activityNameSet : "+MyFirebaseMessagingService.isOnChattingActivity);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MyFirebaseMessagingService.isOnChattingActivity = false;
        Log.d(TAG,"activityNameSet : "+MyFirebaseMessagingService.isOnChattingActivity);
        super.onPause();
    }
}
