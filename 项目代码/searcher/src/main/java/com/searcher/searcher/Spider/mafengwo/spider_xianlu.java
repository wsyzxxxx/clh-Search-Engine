package com.searcher.searcher.Spider.mafengwo;


import com.gargoylesoftware.htmlunit.*;

import com.gargoylesoftware.htmlunit.html.HtmlPage;


import com.searcher.searcher.DataFormat.WebPageData;
import com.searcher.searcher.Spider.Util.Util;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Vector;
import java.util.logging.Level;
import com.searcher.searcher.Spider.Spider;


public class spider_xianlu implements Spider{


    public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {


    }


    @Override
    public Vector<WebPageData> getData() {
      Vector<WebPageData> data=new Vector<>();

        String baseurl = "http://www.mafengwo.cn/sales/";
       HashSet<String> visited=new HashSet<String>();
        HashSet<String> tovisit=new HashSet<String>();
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

           // HtmlPage page = webClient.getPage(url);

            //String pageAsXml = page.asXml();

            // Jsoup解析处理
            //Document doc = Jsoup.parse(pageAsXml);
            Document doc= null;
            try {
                doc = Jsoup.connect(url).get();


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
                boolean first=true;
                for(Element tag:tagsele)
                {

                    if(first) {
                        first=false;
                        continue;
                    }
                    tags.add(tag.text());
                }

                temp.setTags(tags);
                temp.setUrl(url);


                Elements imgs_remote=doc.getElementsByAttributeValue("data-type","salesPhoto").first().getElementsByTag("li");
                System.out.println("****"+imgs_remote.size());

                Vector<String> imgs=new Vector<>();
                for(Element img_remote:imgs_remote)
                {
                    String img=img_remote.getElementsByTag("img").first().attr("src");
                    imgs.add(Util.getImage(img));

                    System.out.println("img:"+img);
                }

                temp.setBase64PictureCode(imgs);





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
        return data;
    }
}





