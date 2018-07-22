package com.searcher.searcher.Spider.mafengwo;


import com.gargoylesoftware.htmlunit.*;

import com.gargoylesoftware.htmlunit.html.HtmlPage;


import com.searcher.searcher.DataFormat.GongLue_Data;
import com.searcher.searcher.DataFormat.WebPageData;
import com.searcher.searcher.Spider.Util.Util;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Vector;
import java.util.logging.Level;
import com.searcher.searcher.Spider.Spider;

public class spider_gonglue implements Spider{


    public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {

    }



    @Override
    public Vector<WebPageData> getData() throws FileNotFoundException {
       Vector<WebPageData> data=new Vector<>();

        String baseurl = "https://www.mafengwo.cn";
        // 屏蔽HtmlUnit等系统 log

        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http.client").setLevel(Level.OFF);


        // HtmlUnit 模拟浏览器
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);              // 启用JS解释器，默认为true
        webClient.getOptions().setCssEnabled(false);                    // 禁用css支持
        webClient.getOptions().setThrowExceptionOnScriptError(false);   // js运行错误时，是否抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(10 * 1000);                   // 设置连接超时时间


        HashSet<String> visited=new HashSet<String>();
        HashSet<String> tovisit=new HashSet<String>();

        String next="/gonglve/ziyouxing/983.html";


        File file=new File("log.txt");


        OutputStreamWriter writer=new OutputStreamWriter(new FileOutputStream(file));


        String url="https://www.mafengwo.cn/gonglve/ziyouxing/983.html";
        int step=0;
        while(step<100) {
            step++;


            url=baseurl+next;
            Document doc= null;
            try {
                HtmlPage page = webClient.getPage(url);
                webClient.waitForBackgroundJavaScript(5 * 1000);               // 等待js后台执行1秒

                String pageAsXml = page.asXml();


                doc = Jsoup.parse(pageAsXml);


                Elements contents=doc.getElementsByClass("sideL");
                //获取信息
                if(contents.size()>0)
                {
                    System.out.println(url);
                    Element content=contents.first();
                    GongLue_Data temp=new GongLue_Data();
                    temp.setContent(content.getElementsByClass("_j_content").first().html());
                    temp.setTitle(content.getElementsByTag("h1").first().html());
                    String img=doc.getElementsByTag("img").first().attr("src");
                    Vector<String> imgs=new Vector<>();
                    imgs.add(Util.getImage(img));
                    temp.setBase64PictureCode(imgs);

                    String des=doc.getElementsByClass("l-topic").first().getElementsByTag("p").first().text();
                    temp.setDescription(des);


                    String pos=doc.getElementsByClass("crumb").first().getElementsByTag("a").get(1).text();
                    temp.setPosition(pos);

                    String date=doc.getElementsByClass("sub-tit").first().getElementsByClass("time").get(1).getElementsByTag("em").first().text();

                    temp.setUrl(url);
                    temp.setDate(date);
                    data.add(temp);



                }
                else
                {
                    System.out.println("不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在不存在");
                }
                visited.add(next);


                //获取可访问连接
                if(false)
                {
                    System.out.println("找不到链接");
                }
                else
                {
                    Elements links=doc.getElementsByTag("a");
                    for(Element link:links)
                    {

                        if(link.hasAttr("href"))
                        {
                            if(!link.attr("href").startsWith("/gonglve/ziyouxing/")||!link.attr("href").contains("html"))
                                continue;

                            String index=link.attr("href");
                            if(visited.contains(index))
                            { //已访问过
                                continue;
                            }
                            else
                            {
                                tovisit.add(index);
                            }
                        }

                    }
                }
            } catch (IOException e) {
                System.out.println("404 "+url);

            }

            if (!tovisit.isEmpty()) {
                for(String nn:tovisit)
                {
                    next=nn;
                    break;
                }
                tovisit.remove(next);
            }
            else
            {
                break;
            }

        }
        webClient.close();
        return data;
    }
}





