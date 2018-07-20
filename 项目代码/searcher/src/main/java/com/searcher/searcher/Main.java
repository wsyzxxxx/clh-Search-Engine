package com.searcher.searcher;

import com.searcher.searcher.DataFormat.WebPageData;
import com.searcher.searcher.Spider.Spider;
import com.searcher.searcher.Spider.mafengwo.*;

import java.util.Vector;

public class Main {
    public static void main(String[] args){
        SearchEngine searchEngine = new SearchEngine("localhost", 9200,"http");
        Spider spider = new spider_xianlu();
        Vector<WebPageData> testData = new Vector<>();

        for (int i = 0; i < 10; i++){
            WebPageData data = new WebPageData();
            Vector<String> tags = new Vector<>();
            tags.add("yes" + i);
            data.setUrl(i + ".html");
            data.setTitle(i + "旅游");
            data.setPrice("100"+i);
            data.setChildPrice(i + 50);
            data.setTags(tags);
            testData.add(data);
        }
        try {
//            searchEngine.createIndex("clh-search-engine");
            searchEngine.updateData("clh-search-engine", testData);
            searchEngine.updateData("clh-search-engine",spider);

            Vector<WebPageData> result = searchEngine.search("店铺",
                    "clh-search-engine",
                    "title", "tags");
            System.out.println("result size = " + result.size());
            for (WebPageData i : result){
                System.out.println(i.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}
