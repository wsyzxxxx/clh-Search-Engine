package com.searcher.searcher.DataFormat;

public class GongLue_Data extends WebPageData {
    String content;
    String description;
    String position;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setContent(String c)
    {
        content=c;
    }
    public String getContent()
    {
        return content;
    }

}
