package io.github.tadoya.din9talk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.quickstart.fcm.MyFirebaseMessagingService;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.tadoya.din9talk.models.Chat;

public class ChattingList extends BaseActivity {

    private static final String TAG = "ChattingList";

    private RecyclerView chatRecyclerView;
    private List<Chat> chatList;
    private ChatViewAdapter chatAdapter;

    public static final String TO_TOKEN = "token";
    public static final String MY_NAME = "myName";

    private Button chatbox_sendButton;
    private EditText chatbox_Text;
    private String toToken;
    private String myToken;
    private String myName;
    private String now = "time";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_list);

        myToken = FirebaseInstanceId.getInstance().getToken();
        toToken = getIntent().getStringExtra(TO_TOKEN);

        if (toToken == null) {
            throw new IllegalArgumentException("Must pass EXTRA_TOKEN");
        }
        myName = SignInActivity.usernameFromEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        chatbox_sendButton = (Button)findViewById(R.id.chatbox_sendButton);
        chatbox_Text = (EditText)findViewById(R.id.chatbox_Text);

        chatRecyclerView = (RecyclerView) findViewById(R.id.chatbox_recycler_list);
        LinearLayoutManager chatLayoutManager = new LinearLayoutManager(getApplicationContext());
        //chatLayoutManager.setReverseLayout(true); 레이아웃 반전
        chatLayoutManager.setStackFromEnd(true); //밑에부터 쌓기
        chatRecyclerView.setLayoutManager(chatLayoutManager);

        chatList = new ArrayList<Chat>();
        chatAdapter = new ChatViewAdapter(getApplicationContext(), chatList);


        chatbox_sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GCMSender(chatbox_Text.getText().toString(), toToken, myName, myToken);
                now = DateFormat.getDateTimeInstance().format(new Date());
                chatList.add(new Chat("", chatbox_Text.getText().toString(), now));
                chatRecyclerView.setAdapter(chatAdapter);
                chatbox_Text.setText("");
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
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("chat");
        if (message != null) {
            now = DateFormat.getDateTimeInstance().format(new Date());
            chatList.add(new Chat(title, message, now));
            chatRecyclerView.setAdapter(chatAdapter);
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
