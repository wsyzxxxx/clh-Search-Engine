package com.searcher.searcher;
import com.searcher.searcher.DataFormat.GongLue_Data;
import com.searcher.searcher.DataFormat.WebPageData;
import com.searcher.searcher.Spider.Spider;
import com.alibaba.fastjson.JSON;
import com.searcher.searcher.DataFormat.WebPageData;
import com.searcher.searcher.Spider.Spider;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;

public class SearchEngine{
    RestHighLevelClient client;
    public SearchEngine(String host, int port, String protocol){
        client = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, protocol)));

    }


    public void createIndex(String indexName) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        client.indices().create(createIndexRequest);
    }

    public void deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        client.indices().delete(deleteIndexRequest);
    }

    public boolean indexExists(String indexName) throws IOException {
        RestClient restClient = client.getLowLevelClient();
        Response response = restClient.performRequest("HEAD", "/" + indexName);
        return response.getStatusLine().getStatusCode() == 200;
    }

    public Vector<WebPageData> search(String keyword, String indexName, Class <? extends WebPageData> cl,String... fieldName) throws IOException, InterruptedException {
        return search(keyword, 100, indexName,cl, fieldName);
    }

    public Vector<WebPageData> search(String keyword, int returnSize, String indexName, Class <? extends WebPageData> cl,String... fieldName) throws IOException, InterruptedException {
        GetRequest getRequest = new GetRequest();

        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, fieldName));
        searchSourceBuilder.size(returnSize);
        searchRequest.source(searchSourceBuilder);


        SearchResponse searchResponse = client.search(searchRequest);

        SearchHits resultSet = searchResponse.getHits();
        System.out.println("resultS et size = " + resultSet.totalHits);


        Vector<WebPageData> result = new Vector<>();
        for(SearchHit i : resultSet){
            WebPageData webPageData =  JSON.parseObject(i.getSourceAsString(), cl);
            result.add(webPageData);
        }
        return result;
    }

    public void updateData(String indexName, Spider... spider) throws IOException {
        Vector<WebPageData> updateContext = new Vector<>();
        for(Spider i : spider){
            Vector<WebPageData> tempData = i.getData();
            updateContext.addAll(tempData);
        }

        updateData(indexName, updateContext);

    }

    public void updateData(String indexName, Vector<WebPageData> data) throws IOException {
        IndicesClient indicesClient = client.indices();
        DeleteIndexRequest clearAllData = new DeleteIndexRequest(indexName);
        indicesClient.delete(clearAllData);
        CreateIndexRequest rebuild = new CreateIndexRequest(indexName);
        indicesClient.create(rebuild);
        Class cl=data.get(0).getClass();

        for(WebPageData i:data){

            String jsonString = JSON.toJSONString(cl.cast(i));
            IndexRequest indexRequest = new IndexRequest(indexName, "doc");
            indexRequest.source(jsonString, XContentType.JSON);
            IndexResponse indexResponse = client.index(indexRequest);
        }
        RefreshRequest refresh = new RefreshRequest(indexName);
        client.indices().refresh(refresh);
    }

    public Vector<WebPageData> getRecommendation(String indexName, Set<String> tags) throws IOException, InterruptedException {
        TreeMap<Integer, Set<WebPageData>> allData = new TreeMap<>();
        HashMap<WebPageData, Integer> dataFreq = new HashMap<>();
        for(String key : tags){
            Vector<WebPageData> tempResult = this.search(key, indexName, WebPageData.class, "tags");
            for (WebPageData data : tempResult){
                if (!dataFreq.containsKey(data)){
                    dataFreq.put(data, 1);
                    if (!allData.containsKey(1)){
                        Set<WebPageData> temp = new TreeSet<>();
                        temp.add(data);
                        allData.put(1, temp);
                    }
                } else {
                    int freq = dataFreq.get(data);

                    allData.get(freq).remove(data);
                    dataFreq.put(data, freq + 1);

                    if (!allData.containsKey(freq + 1)){
                        Set<WebPageData> temp = new TreeSet<>();
                        temp.add(data);
                        allData.put(freq + 1, temp);
                    }

                    if (allData.get(freq).size() == 0){
                        allData.remove(freq);
                    }
                }
            }
        }

        Vector<WebPageData> result = new Vector<>();
        Map.Entry<Integer, Set<WebPageData>> entry = allData.lastEntry();
        while(result.size() < 10 || entry == null){
            assert entry != null;
            for (WebPageData i : entry.getValue()){
                result.add(i);
                if (result.size() == 10) break;
            }
            entry = allData.lowerEntry(entry.getKey());
        }

        assert result.size() <= 10;
        return result;
    }
}
