package com.searcher.searcher.Spider.YouXiaKeSpider;

import com.searcher.searcher.DataFormat.WebPageData;
import com.searcher.searcher.Spider.Spider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Iterator;
import java.util.Vector;

public class YouXiaKeSpider implements Spider{
    @Override
    public Vector<WebPageData> getData() {
        Vector<WebPageData> result = new Vector<>();
        for(int i = 0; i < 1000; ++i) {
            WebPageData webPageData = new WebPageData();
            System.out.println(i);

            try {
                String base = "http://www.youxiake.com/lines.html?id=";
                String url = base + (19000 + i);
                Document Doc = Jsoup.connect(url).get();
                Element price = Doc.getElementById("price");
                if (price == null) {
                    System.out.println(19000 + i + "is illegal");
                } else {
                    webPageData.setUrl(url);
                    webPageData.setTitle(new String(Doc.title().getBytes()));

                    Elements priceList = price.getElementsByClass("price");
                    String adultPrice = ((Element)priceList.get(0)).text().split(" ")[0];
                    String childPrice = ((Element)priceList.get(1)).text().split(" ")[0];

                    webPageData.setPrice(adultPrice);
                    webPageData.setChildPrice(Integer.parseInt(childPrice));

                    Vector<String> webPageTags = new Vector<>();
                    Elements outlineItem = Doc.getElementsByClass("outline-item");
                    Element outline = (Element)outlineItem.get(1);
                    Elements tags = outline.getElementsByClass("line_msg");
                    Iterator var12 = tags.iterator();
                    while(var12.hasNext()) {
                        Element tag = (Element)var12.next();
                        webPageTags.add(tag.text());
                    }
                    webPageData.setTags(webPageTags);
                    result.add(webPageData);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
