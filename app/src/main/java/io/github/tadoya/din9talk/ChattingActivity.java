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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.quickstart.fcm.MyFirebaseMessagingService;

public class ChattingActivity extends BaseActivity {

    private static final String TAG = "ChattingActivity";

    public static final String TO_TOKEN = "token";
    public static final String MY_NAME = "myName";

    private Button sendButton;
    private EditText editText;
    private TextView chatView;
    private String toToken;
    private String myToken;
    private String myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        myToken = FirebaseInstanceId.getInstance().getToken();
        toToken = getIntent().getStringExtra(TO_TOKEN);

        if (toToken == null) {
            throw new IllegalArgumentException("Must pass EXTRA_TOKEN");
        }

        myName = SignInActivity.usernameFromEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        chatView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        sendButton = (Button)findViewById(R.id.sendButton);

        chatView.setText(MyFirebaseMessagingService.chat);
        chatView.setMovementMethod(new ScrollingMovementMethod());


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GCMSender sender = new GCMSender(editText.getText().toString(), toToken, myName, myToken);
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

    @Override
    protected void onStop() {
        MyFirebaseMessagingService.chat ="";
        Log.d(TAG,"activityNameSet(stop) : "+MyFirebaseMessagingService.isOnChattingActivity);
        super.onStop();
    }
}
