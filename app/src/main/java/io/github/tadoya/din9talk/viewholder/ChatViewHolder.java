package io.github.tadoya.din9talk.viewholder;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import io.github.tadoya.din9talk.R;
import io.github.tadoya.din9talk.models.Chat;
import io.github.tadoya.din9talk.models.ChatList;

/**
 * Created by choiseongsik on 2016. 7. 11..
 */

public class ChatViewHolder extends RecyclerView.ViewHolder{

    private TextView chatbox_username, chatbox_item, chatbox_time;
    private LinearLayout chatbox_layout;

    public ChatViewHolder(View itemView){
        super(itemView);
        chatbox_layout = (LinearLayout)itemView.findViewById(R.id.chatbox_layout);
        chatbox_username = (TextView) itemView.findViewById(R.id.chatbox_username);
        chatbox_item = (TextView) itemView.findViewById(R.id.chatbox_item);
        chatbox_time = (TextView) itemView.findViewById(R.id.chatbox_time);

    }
    public void bindToItem(Chat chat){
        chatbox_username.setText(chat.getUserName());
        chatbox_item.setText(chat.getMessage());
        chatbox_time.setText(chat.getCurrentTime());

    }
    public void bindToItem(ChatList chatList){
        chatbox_username.setText(chatList.getRoomID());
        chatbox_item.setText(chatList.getMessage());
        chatbox_time.setText(chatList.getChatTimeT());
    }

    public TextView getChatbox_username(){
        return chatbox_username;
    }
    public TextView getChatbox_item(){
        return chatbox_item;
    }
    public TextView getChatbox_time(){
        return chatbox_time;
    }
    public LinearLayout getChatbox_layout(){ return chatbox_layout; }
}
