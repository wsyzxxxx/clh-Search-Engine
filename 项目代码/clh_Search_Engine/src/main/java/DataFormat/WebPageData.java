package DataFormat;

import java.util.Vector;

public class WebPageData {
    private String title;
    private String url;
    private int price;
    private int childPrice;
    private Vector<String> tags;
    private Vector<byte[]> base64PictureCode;

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


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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



    public Vector<byte[]> getBase64PictureCode() {
        return base64PictureCode;
    }

    public void setBase64PictureCode(Vector<byte[]> base64PictureCode) {
        this.base64PictureCode = base64PictureCode;
    }

}
