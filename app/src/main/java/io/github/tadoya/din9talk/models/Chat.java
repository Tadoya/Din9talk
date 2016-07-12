package io.github.tadoya.din9talk.models;

/**
 * Created by choiseongsik on 2016. 7. 11..
 */

public class Chat {
    private String userName;
    private String message;
    private String currentTime;

    public Chat() {

    }

    public Chat(String userName, String message, String currentTime) {
        this.userName = userName;
        this.message = message;
        this.currentTime = currentTime;
    }

    public String getUserName() { return userName; }
    public String getMessage() { return message; }
    public String getCurrentTime() { return currentTime; }

    public void setUserName(String userName){ this.userName = userName; }
    public void setMessage(String message){ this.message = message; }
    public void setCurrentTime(String currentTime){ this.currentTime = currentTime; }
}
