package com.wang.tim.xml_json;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by twang on 2015/1/12.
 */
public class XMLParserHandler extends DefaultHandler {
    private String nodeName;
    private StringBuilder id;
    private StringBuilder name;
    private StringBuilder version;

    @Override
    public void startDocument() throws SAXException {
        id = new StringBuilder();
        name = new StringBuilder();
        version = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //记录当前节点名
        nodeName = localName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        //根据当前节点名判断将内容添加到哪个StringBuilder对象中
        if("id".equals(nodeName)){
            id.append(ch,start,length);
        }else if("name".equals(nodeName)){
            name.append(ch,start,length);
        }else if("version".equals(nodeName)){
            version.append(ch,start,length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if("app".equals(localName)){
            Log.e("XMLParserHandler","id is "+id.toString().trim());
            Log.e("XMLParserHandler","name is "+name.toString().trim());
            Log.e("XMLParserHandler","version is "+version.toString().trim());
            //最后要将StringBuilder清空
            id.setLength(0);
            name.setLength(0);
            version.setLength(0);
        }
    }
}
