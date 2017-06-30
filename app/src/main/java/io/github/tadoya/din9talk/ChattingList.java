package io.github.tadoya.din9talk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.quickstart.fcm.MyFirebaseMessagingService;

import java.text.DateFormat;
import java.util.Date;

import io.github.tadoya.din9talk.models.Chat;
import io.github.tadoya.din9talk.models.ChatList;
import io.github.tadoya.din9talk.realmDB.ChatRecyclerViewAdapter;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ChattingList extends BaseActivity {

    private static final String TAG = "ChattingList";

    private Realm realm;
    private RecyclerView chatRecyclerView;
    private ChatRecyclerViewAdapter chatAdapter;

    public static final String TO_TOKEN = "token";
    public static final String ROOM_NAME = "room_name";

    private Button chatbox_sendButton;
    private EditText chatbox_Text;
    private String toToken;
    private String myToken;
    private String myName;
    private String now = "";
    private String roomName;

    private RealmResults<Chat> result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_list);


        realm = Realm.getDefaultInstance();

        myToken = FirebaseInstanceId.getInstance().getToken();
        toToken = getIntent().getStringExtra(TO_TOKEN);
        roomName = getIntent().getStringExtra(ROOM_NAME);

        Log.d(TAG, "roomName = "+ roomName);

        if (toToken == null) {
            throw new IllegalArgumentException("Must pass TO_TOKEN");
        }
        if (roomName == null) {
            throw new IllegalArgumentException("Must pass ROOM_NAME");
        }

        myName = SignInActivity.usernameFromEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        chatbox_sendButton = (Button)findViewById(R.id.chatbox_sendButton);
        chatbox_sendButton.setEnabled(false);
        chatbox_Text = (EditText)findViewById(R.id.chatbox_Text);

        chatRecyclerView = (RecyclerView) findViewById(R.id.chatbox_recycler_list);

        chatbox_Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(chatbox_Text.getText().length()>0) chatbox_sendButton.setEnabled(true);
                else chatbox_sendButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        setUpRecyclerView();

        result.addChangeListener(new RealmChangeListener<RealmResults<Chat>>() {
            @Override
            public void onChange(RealmResults<Chat> element) {
                chatRecyclerView.setAdapter(chatAdapter);
            }
        });

        chatbox_sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GCMSender(chatbox_Text.getText().toString(), toToken, myName, myToken);

                now = DateFormat.getDateTimeInstance().format(new Date());
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Chat sendChat = realm.createObject(Chat.class);
                        sendChat.setUserName("");
                        sendChat.setMessage(chatbox_Text.getText().toString());
                        sendChat.setCurrentTime(now);
                        sendChat.setRoomName(roomName);

                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        chatRecyclerView.setAdapter(chatAdapter);
                        chatbox_Text.setText("");
                    }
                });
            }
        });
    }

    private void setUpRecyclerView() {
        LinearLayoutManager chatLayoutManager = new LinearLayoutManager(getApplicationContext());
        chatLayoutManager.setStackFromEnd(true); //밑에부터 쌓기
        chatRecyclerView.setLayoutManager(chatLayoutManager);

        result = realm.where(Chat.class).equalTo("roomName", roomName).findAllAsync();
        //result_chatList = realm.where(ChatList.class).equalTo("roomID", roomName).findAllAsync();

        chatAdapter = new ChatRecyclerViewAdapter(this, result);

        chatRecyclerView.setAdapter(chatAdapter);

        chatRecyclerView.setHasFixedSize(true);
        //chatRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
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
        final String title = intent.getStringExtra("title");
        final String message = intent.getStringExtra("chat");
        if (message != null) {
            now = DateFormat.getDateTimeInstance().format(new Date());
            //roomName = title; 임시 추후 UID로
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Chat receiveChat = realm.createObject(Chat.class);
                    receiveChat.setUserName(title);
                    receiveChat.setMessage(message);
                    receiveChat.setCurrentTime(now);
                    receiveChat.setRoomName(roomName);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    chatRecyclerView.setAdapter(chatAdapter);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        MyFirebaseMessagingService.isOnChattingActivity = true;
        MyFirebaseMessagingService.chattingAcitivityName = roomName;
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

        if(!now.isEmpty()) {
            final ChatList chatList = new ChatList(roomName,
                    realm.where(Chat.class)
                            .equalTo("roomName", roomName)
                            .findAll().last().getMessage(), now);

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ChatList realmChatList = realm.copyToRealm(chatList);
                    if(realm.where(ChatList.class).equalTo("roomID",roomName).findAll().size()>1)
                    realm.where(ChatList.class).equalTo("roomID", roomName).findFirst().deleteFromRealm();
                }
            });
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
