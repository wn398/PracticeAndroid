package com.wang.tim.xml_json;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;


public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int SHOW_RESPONSE = 1;
    private Button parserFromXMLBtn;
    private Button parserFromJSONBtn;
    private Button getWithHttpUrlconnection;
    private Button getWithHttpClient;
    private TextView contentView;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parserFromXMLBtn = (Button)findViewById(R.id.parserXMLBtn);
        parserFromJSONBtn = (Button) findViewById(R.id.parserJSONBtn);
        getWithHttpUrlconnection = (Button) findViewById(R.id.getWithHttpUrlConnection);
        getWithHttpClient = (Button) findViewById(R.id.getWithHttpClient);
        getWithHttpClient.setOnClickListener(this);
        getWithHttpUrlconnection.setOnClickListener(this);
        parserFromJSONBtn.setOnClickListener(this);
        parserFromXMLBtn.setOnClickListener(this);
        contentView = (TextView) findViewById(R.id.contentView);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case SHOW_RESPONSE:
                        contentView.setText((String)msg.obj);
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getWithHttpUrlConnection:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       NetWorkUtil.getResponseWithHttpUrlConnection(handler, "http://www.baidu.com");
                    }
                }).start();
                break;
            case R.id.getWithHttpClient:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        NetWorkUtil.getResponseWithHttpClient(handler,"http://www.163.com");
                    }
                }).start();
                break;
            case R.id.parserXMLBtn://android中对网络资源的请求不能放在主线程中，以防网络没响应，页面假死
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //String result = NetWorkUtil.getResponseWithHttpUrlConnection(null,"http://10.30.178.11:99/get_data.xml");
                        String result = NetWorkUtil.getResponseWithHttpClient(null, "http://10.30.178.11:99/get_data.xml");
                        parseXMLWithPull(result);
                        parseXMLWithSAX(result);
                    }
                }).start();
                break;
            case R.id.parserJSONBtn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = NetWorkUtil.getResponseWithHttpUrlConnection(null, "http://10.30.178.11:99/get_data.json");
                        parseJSONWithJSONObject(result);
                        parseJSONWithGSON(result);
                    }
                }).start();
                break;
            default:
                break;
        }

    }
    /*解析文档为：
<apps>
    <app>
        <id>1</id>
        <name>Google Maps</name>
        <version>1.0</version>
    </app>
    <app>
        <id>2</id>
        <name>Chrome</name>
        <version>2.1</version>
    </app>
    <app>
        <id>3</id>
        <name>Google Play</name>
        <version>2.3</version>
    </app>
</apps>

     */
    //用pull解析XML
    private void parseXMLWithPull(String xmlData){
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while(eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch (eventType){
                    //开始解析某个结点
                    case XmlPullParser.START_TAG:{
                        if("id".equals(nodeName)){
                            id = xmlPullParser.nextText();
                        }else if("name".equals(nodeName)){
                            name = xmlPullParser.nextText();
                        }else if ("version".equals(nodeName)){
                            version = xmlPullParser.nextText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        //完成解析某个结点
                        if("app".equals(nodeName)){
                            Log.e("MainActivity","id is "+id);
                            Log.e("MainActivity","name is "+name);
                            Log.e("MainActivity","version is "+version);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //用sax解析xml，不过要实现自己的ContentHandler
    private void parseXMLWithSAX(String xmlData){
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            XMLParserHandler xmlParserHandler = new XMLParserHandler();
            //将handle实例设置到XMLReader中
            xmlReader.setContentHandler(xmlParserHandler);
            //开始解析
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*要解析的Json数据
    [{"id":"5","version":"5.5","name":"Angry Birds"},
        {"id":"6","version":"7.0","name":"Clash of Clans"},
        {"id":"7","version":"3.5","name":"Hey Day"}]
     */
    //使用系统自带的JSONobject解析
    private void parseJSONWithJSONObject(String JSONObject) {
        try {
            JSONArray jsonArray = new JSONArray(JSONObject);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                Log.e(TAG,"id is "+id);
                Log.e(TAG,"name is "+name);
                Log.e(TAG,"version is "+version);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //使用GSON库解析
    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        List<App> appList = gson.fromJson(jsonData,new TypeToken<List<App>>(){}.getType());
        for(App app:appList){
            Log.e(TAG,"id is "+app.getId());
            Log.e(TAG,"name is "+app.getName());
            Log.e(TAG,"version is "+app.getVersion());
        }
    }
}
