package com.wang.tim.contactmanager;

import android.provider.BaseColumns;

/**
 * Created by twang on 2014/12/24.
 */
public class ContactColumnInfo implements BaseColumns {
    //列名
    public static final String NAME = "name";
    public static final String MOBILENUM = "mobileNumber";
    public static final String HOMENUM = "homeNumber";
    public static final String ADDRESS = "address";
    public static final String EMAIL = "email";
    public static final String BLOG = "blog";
    //列索引值
    public static final int _ID_COLUMN = 0;
    public static final int NAME_COLUMN = 1;
    public static final int MOBILNUM_COLUMN = 2;
    public static final int HOMENUM_COLUMN = 3;
    public static final int ADDRESS_COLUMN = 4;
    public static final int EMAIL_COLUMN = 5;
    public static final int BLOG_COLUMN = 6;

    public ContactColumnInfo() {

    }

    //查询结果
    public static final String[] RESULTS = {
            _ID,
            NAME,
            MOBILENUM,
            HOMENUM,
            ADDRESS,
            EMAIL,
            BLOG,
    };
}
