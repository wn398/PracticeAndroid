package com.wang.tim.xml_json;

/**
 * Created by twang on 2015/1/13.
 */
public class NetWorkUtilTest {
    public static void main(String[] args){
        String str = NetWorkUtil.getResponseWithHttpClient(null,"http://127.0.0.1:99/get_data.xml");
        System.out.println(str);
    }

}
