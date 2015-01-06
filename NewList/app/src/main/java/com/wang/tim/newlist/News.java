package com.wang.tim.newlist;

/**
 * Created by twang on 2015/1/6.
 */
public class News {
    private String title;
    private String context;
    public News(String title,String context){
        this.title = title;
        this.context = context;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
