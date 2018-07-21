package com.searcher.searcher.DataFormat;

import java.util.Vector;

public class WebPageData {
    private String title;
    private String url;
    private String price;
    private int childPrice;
    private Vector<String> tags;
    private Vector<String> base64PictureCode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
//123123

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String  getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public Vector<String> getTags() {
        return tags;
    }

    public void setTags(Vector<String> tags) {
        this.tags = tags;
    }


    public int getChildPrice() {
        return childPrice;
    }

    public void setChildPrice(int childPrice) {
        this.childPrice = childPrice;
    }



    public Vector<String> getBase64PictureCode() {
        return base64PictureCode;
    }

    public void setBase64PictureCode(Vector<String> base64PictureCode) {
        this.base64PictureCode = base64PictureCode;
    }

}
