package com.wang.tim.yourweather.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by twang on 2015/1/20.
 */
public class NetWorkUtil {
    public static String getDataFromNet(String address){
        try {
            URL url = new URL(address);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void getFileFromNet(String address,File file){
        try {
            URL url = new URL(address);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            DataInputStream input = new DataInputStream(httpURLConnection.getInputStream());
            DataOutputStream output = new DataOutputStream(new FileOutputStream(
                    file));
            byte[] buffer = new byte[1024 * 8];
            int count = 0;
            while ((count = input.read(buffer)) > 0) {
                output.write(buffer, 0, count);
            }
            output.close();
            input.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
