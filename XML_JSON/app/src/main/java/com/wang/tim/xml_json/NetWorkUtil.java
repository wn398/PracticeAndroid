package com.wang.tim.xml_json;

import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by twang on 2015/1/12.
 */
public class NetWorkUtil {
    private static final int SHOW_RESPONSE = 1;

    //用传统的HttpUrlConnection请求网络
    public static String getResponseWithHttpUrlConnection(Handler handler,String url) {

        try {
            URL mUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) mUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            //httpURLConnection.setConnectTimeout(8000);
            //httpURLConnection.setReadTimeout(8000);

            InputStream in = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            if(handler !=null) {
                Message message = new Message();
                message.what = SHOW_RESPONSE;
                message.obj = response.toString();
                handler.sendMessage(message);
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //用HttpClient请求网络
    public static String getResponseWithHttpClient(Handler handler,String url){
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                String response = EntityUtils.toString(httpEntity,"utf-8");
                if(handler !=null) {
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = response;
                    handler.sendMessage(message);
                }
                return response;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
