import DataFormat.WebPageData;
import Spider.Spider;
import Spider.YouXiaKeSpider.YouXiaKeSpider;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args){
        SearchEngine searchEngine = new SearchEngine("localhost", 9200,"http");
        Spider spider = new YouXiaKeSpider();
        Vector<WebPageData> testData = new Vector<>();

        for (int i = 0; i < 10; i++){
            WebPageData data = new WebPageData();
            Vector<String> tags = new Vector<>();
            tags.add("yes" + i);
            data.setUrl(i + ".html");
            data.setTitle(i + "traveal");
            data.setPrice(i + 100);
            data.setChildPrice(i + 50);
            data.setTags(tags);
            testData.add(data);
        }
        try {
            //searchEngine.createIndex("clh-search-engine");
            searchEngine.updateData("clh-search-engine", testData);

            Vector<WebPageData> result = searchEngine.search("traveal",
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
