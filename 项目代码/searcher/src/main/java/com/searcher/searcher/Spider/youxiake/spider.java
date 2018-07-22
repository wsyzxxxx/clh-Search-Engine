package com.searcher.searcher.Spider.youxiake;

import com.searcher.searcher.DataFormat.WebPageData;
import com.gargoylesoftware.htmlunit.*;

import com.gargoylesoftware.htmlunit.html.HtmlPage;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Vector;
import java.util.logging.Level;
import com.searcher.searcher.Spider.Spider;

public class spider implements Spider{

    private Vector<WebPageData> data;
    public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {


    }


    @Override
    public Vector<WebPageData> getData() throws IOException {
        // 屏蔽HtmlUnit等系统 log
        //LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");

        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http.client").setLevel(Level.OFF);

        String baseurl = "http://www.mafengwo.cn/sales/";
        // HtmlUnit 模拟浏览器
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);              // 启用JS解释器，默认为true
        webClient.getOptions().setCssEnabled(false);                    // 禁用css支持
        webClient.getOptions().setThrowExceptionOnScriptError(false);   // js运行错误时，是否抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(10 * 1000);                   // 设置连接超时时间
        HashSet<String> visited=new HashSet<String>();
        HashSet<String> tovisit=new HashSet<String>();
        webClient.waitForBackgroundJavaScript(50 * 1000);               // 等待js后台执行30秒
        String next="";
        String url="http://www.mafengwo.cn/sales/";
        int step=0;
        while(step<100) {
            step++;

            if(step!=1)
            {
                url=baseurl+next+".html";
            }
            System.out.println("url="+url);

          //  HtmlPage page = webClient.getPage(url);

          //  String pageAsXml = page.asXml();

            // Jsoup解析处理

            Document doc=Jsoup.connect(url).get();
            //Document doc = Jsoup.parse(pageAsXml);


            //获取信息
            if(!next.contains("-")&&doc.getElementsByClass("intro-r").size()>0)
            {

                Element useful=doc.getElementsByClass("intro-r").first();
                WebPageData temp=new WebPageData();
                temp.setTitle(useful.getElementsByTag("h1").first().text());
                System.out.println("title="+useful.getElementsByTag("h1").first().text());
                temp.setPrice(useful.getElementsByTag("Strong").first().text());
                System.out.println("price="+useful.getElementsByTag("Strong").first().text());
                Elements tagsele=useful.getElementsByClass("sales-title").first().getElementsByTag("div").first().getElementsByTag("span");


                Vector<String> tags=new Vector<String>();
                for(Element tag:tagsele)
                {
                    tags.add(tag.text());
                    System.out.println("**tag="+tag.text());
                }
                temp.setUrl(url);


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
                        if(!link.attr("href").startsWith("/sales")||!link.attr("href").contains("html"))
                            continue;

                        String index=link.attr("href").split(".h")[0].substring(6);
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
        return data;
    }
}





