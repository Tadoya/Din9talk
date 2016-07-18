package io.github.tadoya.din9talk.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;


import io.github.tadoya.din9talk.ChattingList;
import io.github.tadoya.din9talk.MainActivity;
import io.github.tadoya.din9talk.models.ChatList;
import io.github.tadoya.din9talk.realmDB.ChatListRecyclerAdapter;
import io.github.tadoya.din9talk.realmDB.DividerItemDecoration;
import io.github.tadoya.din9talk.viewholder.ChatViewHolder;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by choiseongsik on 2016. 7. 5..
 */

public class ChattingListFragment extends ListFragment {

    Realm realm;
    ChatListRecyclerAdapter chatListAdapter;

    public ChattingListFragment() {
    }


    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager chatListLayoutManager = new LinearLayoutManager(getActivity());
        chatListLayoutManager.setReverseLayout(true); // 레이아웃 반전
        chatListLayoutManager.setStackFromEnd(true); //밑에부터 쌓기
        mRecycler.setLayoutManager(chatListLayoutManager);
        RealmResults<ChatList> result = realm.where(ChatList.class).isNotNull("roomID").findAllAsync();

        chatListAdapter = new ChatListRecyclerAdapter(getActivity(), result) {
            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, ChatList model, int position) {
                final String roomName = model.getRoomID();

                //임시
                final String toToken = MainActivity.user.getToken();

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ChattingList.class);
                        intent.putExtra(ChattingList.TO_TOKEN, toToken);
                        intent.putExtra(ChattingList.ROOM_NAME, roomName);
                        startActivity(intent);
                    }
                });
            }
        };
        mRecycler.setAdapter(chatListAdapter);



        mRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }
}
