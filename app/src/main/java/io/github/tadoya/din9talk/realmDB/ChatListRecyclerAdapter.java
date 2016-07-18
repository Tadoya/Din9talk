package io.github.tadoya.din9talk.realmDB;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import io.github.tadoya.din9talk.R;
import io.github.tadoya.din9talk.models.ChatList;
import io.github.tadoya.din9talk.viewholder.ChatViewHolder;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by choiseongsik on 2016. 7. 18..
 */

public abstract class ChatListRecyclerAdapter extends RealmRecyclerViewAdapter<ChatList, ChatViewHolder> {
    ChatList chatList;

    public ChatListRecyclerAdapter(Context context, OrderedRealmCollection<ChatList> data) {
        super(context ,data, true);
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.chatbox_recyclerview, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        chatList = getData().get(position);
        holder.bindToItem(chatList);

        populateViewHolder(holder, chatList, position);
    }
    abstract protected void populateViewHolder(ChatViewHolder viewHolder, ChatList model, int position);
}
