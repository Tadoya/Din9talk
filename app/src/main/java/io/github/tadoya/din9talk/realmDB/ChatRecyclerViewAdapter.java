/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.tadoya.din9talk.realmDB;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import io.github.tadoya.din9talk.R;
import io.github.tadoya.din9talk.models.Chat;
import io.github.tadoya.din9talk.viewholder.ChatViewHolder;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


public class ChatRecyclerViewAdapter extends RealmRecyclerViewAdapter<Chat, ChatViewHolder>   {

    Chat chat;
    private Context context;
    private int align;

    public ChatRecyclerViewAdapter(Context context, OrderedRealmCollection<Chat> data) {
        super(context ,data, true);
        this.context = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.chatbox_recyclerview, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        chat = getData().get(position);

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
        holder.getChatbox_item().setLongClickable(true);
        holder.getChatbox_item().setTextIsSelectable(true);
        holder.getChatbox_item().setLinksClickable(true);
    }

}