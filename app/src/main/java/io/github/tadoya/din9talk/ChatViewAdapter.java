package io.github.tadoya.din9talk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.tadoya.din9talk.models.Chat;
import io.github.tadoya.din9talk.viewholder.ChatViewHolder;

/**
 * Created by choiseongsik on 2016. 7. 11..
 */

public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    List<Chat> chats;
    Chat chat;
    private Context context;
    private int align;

    public ChatViewAdapter(Context context,List<Chat> chatList) {
        this.chats = chatList;
        this.context = context;
    }



    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatbox_recyclerview,null);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        chat = chats.get(position);

        holder.bindToItem(chat);

        if(chat.getUserName().isEmpty()){
            holder.getChatbox_item().setBackground(context.getResources().getDrawable(R.drawable.draw_chatbox_s));
            align = Gravity.RIGHT;
        }
        else{
            holder.getChatbox_item().setBackground(context.getResources().getDrawable(R.drawable.draw_chatbox_r));
            align = Gravity.LEFT;
        }
        holder.getChatbox_layout().setGravity(align);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
