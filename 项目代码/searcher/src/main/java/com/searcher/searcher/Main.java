package com.searcher.searcher;

import com.searcher.searcher.DataFormat.GongLue_Data;
import com.searcher.searcher.DataFormat.WebPageData;

import com.searcher.searcher.Spider.Spider;
import com.searcher.searcher.Spider.mafengwo.*;
import com.vividsolutions.jts.awt.PointShapeFactory;

import java.util.Scanner;
import java.util.Vector;

public class Main {
    public static void main(String[] args){
        SearchEngine searchEngine = new SearchEngine("localhost", 9200,"http");
        Spider spider = new spider_xianlu();
        Spider spider1=new spider_gonglue();
        Vector<WebPageData> testData = new Vector<>();

        Vector<WebPageData> testData2=new Vector<>();

        for(int j=0;j<10;j++)
        {
            GongLue_Data temp=new GongLue_Data();
            temp.setTitle("攻略"+j);
            temp.setContent("内容"+j);
            testData2.add(temp);
        }

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
          //  searchEngine.createIndex("gonglue");
          //  searchEngine.updateData("gonglue", testData2);

            searchEngine.updateData("gonglue", spider1);

            //     searchEngine.updateData("clh-search-engine",spider);

            Vector<WebPageData> result = searchEngine.search("一",
                    "gonglue",testData2.get(0).getClass(),
                    "title");
            System.out.println("result size = " + result.size());
            for (WebPageData i : result){
                System.out.println(((GongLue_Data)i).getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}
