import DataFormat.WebPageData;
import Spider.Spider;
import Spider.YouXiaKeSpider.YouXiaKeSpider;
import java.util.Vector;

public class Main {
    public static void main(String[] args){
        SearchEngine searchEngine = new SearchEngine("localhost", 9200,"http");
        Spider spider = new YouXiaKeSpider();
        Vector<WebPageData> testData = new Vector<>();

        for (int i = 0; i < 10; i++){
            WebPageData data = new WebPageData();
            Vector<String> tags = new Vector<>();
            tags.add("yes " + i);
            data.setUrl(i + ".html");
            data.setTitle(i + "旅游");
            data.setPrice(i + 100);
            data.setChildPrice(i + 50);
            data.setTags(tags);
            testData.add(data);
        }
        try {
            //searchEngine.createIndex("clh-search-engine");
            searchEngine.updateData("clh-search-engine", testData);

            Vector<WebPageData> result = searchEngine.search("yes",
                    "clh-search-engine",
                    "title", "tags");
            System.out.println("result size = " + result.size());
            for (WebPageData i : result){
                System.out.println(i.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
