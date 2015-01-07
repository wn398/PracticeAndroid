package com.wang.tim.chatview;

/**
 * Created by twang on 2015/1/5.
 */
public class Message {
    public static final int RECEIVE = 0;
    public static final int SEND = 1;

    public String message;
    public int type;

    public Message(String message, int type){
        this.message = message;
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
