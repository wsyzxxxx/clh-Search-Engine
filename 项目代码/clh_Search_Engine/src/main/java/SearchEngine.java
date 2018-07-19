import DataFormat.WebPageData;
import Spider.Spider;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.*;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

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

    public Vector<WebPageData> search(String keyword, String indexName, String... fieldName) throws IOException, InterruptedException {
        // TODO: implement
        GetRequest getRequest = new GetRequest();

        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, fieldName));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);

        SearchHits resultSet = searchResponse.getHits();
        System.out.println("resultS et size = " + resultSet.totalHits);

        Vector<WebPageData> result = new Vector<>();
        for(SearchHit i : resultSet){
            WebPageData webPageData =  JSON.parseObject(i.getSourceAsString(), WebPageData.class);
            result.add(webPageData);
        }
        return result;
    }

    public void updateData(String indexName, Spider spider) throws IOException {
        // TODO: implement
        Vector<WebPageData> updateContext = spider.getData();
        updateData(indexName, updateContext);

    }

    public void updateData(String indexName, Vector<WebPageData> data) throws IOException {
        IndicesClient indicesClient = client.indices();
        DeleteIndexRequest clearAllData = new DeleteIndexRequest(indexName);
        indicesClient.delete(clearAllData);
        CreateIndexRequest rebuild = new CreateIndexRequest(indexName);
        indicesClient.create(rebuild);

        for(WebPageData i : data){
            String jsonString = JSON.toJSONString(i);
            IndexRequest indexRequest = new IndexRequest(indexName, "doc");
            indexRequest.source(jsonString, XContentType.JSON);
            IndexResponse indexResponse = client.index(indexRequest);
            System.err.println(indexResponse.toString());
        }
    }
}
