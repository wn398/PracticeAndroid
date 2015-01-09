package com.wang.tim.databasetest;

/**
 * Created by twang on 2015/1/8.
 */
public class Book {
    private String name;
    private int pages;
    private int price;
    public Book(String name,int pages,int price){
        this.name = name;
        this.pages = pages;
        this.price = price;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
