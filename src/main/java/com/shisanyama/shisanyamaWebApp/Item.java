package com.shisanyama.shisanyamaWebApp;

public class Item {

    private double price;
    private String title;
    private String url;


    public Item(double price, String title, String url) {
        this.price = price;
        this.title = title;
        this.url = url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Item{" +
                "price=" + price +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
