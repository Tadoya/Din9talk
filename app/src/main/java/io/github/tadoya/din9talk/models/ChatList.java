package io.github.tadoya.din9talk.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by choiseongsik on 2016. 7. 18..
 */

public class ChatList extends RealmObject {

    private String roomID;
    //private String userName;
    private String message;
    private String chatTime;

    public ChatList(){

    }
    public ChatList(String roomID, String message, String time){
        //this.userName = userName;
        this.roomID = roomID;
        this.message = message;
        this.chatTime = time;
    }

    public void setRoomID(String roomID)  {this.roomID = roomID; }
    //public void setUserName(String userName) { this.userName = userName; }
    public void setMessage(String message)  {this.message = message; }
    public void setChatTime(String chatTime)    {this.chatTime = chatTime; }

    public String getRoomID()   { return roomID; }
    //public String getUserName() { return userName; }
    public String getMessage() { return  message; }
    public String getChatTimeT() { return chatTime; }

}
