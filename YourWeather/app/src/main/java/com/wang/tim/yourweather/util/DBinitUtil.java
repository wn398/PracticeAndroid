package com.wang.tim.yourweather.util;

import com.wang.tim.yourweather.DB.WeatherDB;
import com.wang.tim.yourweather.model.City;
import com.wang.tim.yourweather.model.County;
import com.wang.tim.yourweather.model.Province;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;

/**
 * Created by twang on 2015/1/20.
 */
public class DBinitUtil {

    public static void initDB(WeatherDB weatherDB,InputStream inputStream){
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(inputStream,"UTF-8");
            int evntType = xmlPullParser.getEventType();
            String province_id=null;
            String city_id=null;
            String country_id=null;
            while(evntType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch (evntType){
                    case XmlPullParser.START_TAG:{
                        if("province".equals(nodeName)){
                            String id = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(), "id");
                            String name = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(), "name");
                            Province province = new Province();
                            province.setId(Integer.parseInt(id));
                            province.setName(name);
                            weatherDB.saveProvince(province);
                            province_id = id;
                        }else if("city".equals(nodeName)){
                            String id = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(),"id");
                            String name = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(),"name");
                            City city = new City();
                            city.setId(Integer.parseInt(id));
                            city.setName(name);
                            city.setProvince_id(Integer.parseInt(province_id));
                            weatherDB.saveCity(city);
                            city_id=id;
                        }else if("county".equals(nodeName)){
                            String id = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(),"id");
                            String name = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(),"name");
                            String code = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(),"weatherCode");
                            County country = new County();
                            country.setId(Integer.parseInt(id));
                            country.setName(name);
                            country.setCountry_code(Integer.parseInt(code));
                            country.setCity_id(Integer.parseInt(city_id));
                            weatherDB.saveCountry(country);
                        }

                    }
                    case XmlPullParser.END_TAG:{

                    }
                    default:
                        break;
                }
                evntType = xmlPullParser.next();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
