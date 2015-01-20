package com.wang.tim.yourweather.model;

/**
 * Created by twang on 2015/1/19.
 */
public class County {
    private int id;
    private int country_code;
    private String name;
    private int city_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountry_code() {
        return country_code;
    }

    public void setCountry_code(int country_code) {
        this.country_code = country_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }
}
