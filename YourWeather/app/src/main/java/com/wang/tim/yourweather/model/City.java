package com.wang.tim.yourweather.model;

/**
 * Created by twang on 2015/1/19.
 */
public class City {
    private int id;

    private String name;
    private int province_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
