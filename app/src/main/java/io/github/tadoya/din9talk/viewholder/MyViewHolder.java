package io.github.tadoya.din9talk.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.github.tadoya.din9talk.R;
import io.github.tadoya.din9talk.models.User;


public class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView item_title;


    public MyViewHolder(View itemView) {
        super(itemView);
        item_title = (TextView)itemView.findViewById(R.id.item_title);
    }
    public void bindToItem(User user){
        item_title.setText(user.getUserName());
    }

    public TextView getItem_title(){
        return item_title;
    }
}
