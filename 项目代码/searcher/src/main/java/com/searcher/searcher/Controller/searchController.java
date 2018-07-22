package com.searcher.searcher.Controller;

import com.searcher.searcher.DataFormat.GongLue_Data;
import com.searcher.searcher.DataFormat.WebPageData;
import com.searcher.searcher.SearchEngine;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.common.recycler.Recycler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Vector;

@Controller
@EnableAutoConfiguration
public class searchController {
    private SearchEngine searchEngine = new SearchEngine("localhost", 9200,"http");

    @RequestMapping("/search")
    String searchs(@Param("keyword") String keyword, @Param("type") Long type,@Param("curr") Integer curr, Model model) throws IOException, InterruptedException {
        model.addAttribute("keyword",keyword);

        byte[] a={1,2,3,4};




        //验证用户信息
        UserDetails userDetails;
        if(SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal() instanceof UserDetails)
        {
            userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            model.addAttribute("name",userDetails.getUsername());

            model.addAttribute("state",1);

        }
        else
        {
            model.addAttribute("state",0);
        }



        int limit=9;
        List<String> jingdian=new ArrayList<>();//景点列表
        Vector<WebPageData> xianlu,gonglue;
        gonglue= searchEngine.search(keyword,"gonglue",GongLue_Data.class,"title");

        xianlu= searchEngine.search(keyword,"clh-search-engine",WebPageData.class,"title","tags");






        for(int i=41;i<99;i++)
        {
            jingdian.add(String.valueOf(i));
        }



        if(type==4)//搜索景点
        {
            model.addAttribute("count",jingdian.size());
            model.addAttribute("count", jingdian.size());//结果总数
            model.addAttribute("curr",curr); //当前页码
            List<String> listofjingdian=new ArrayList<>(); //为了分页
            int start,end;
            start=curr*limit-limit;
            if(curr*10-1<jingdian.size())
            {
                end=curr*limit;
            }
            else
            {
                end=jingdian.size();
            }

            listofjingdian=jingdian.subList(start,end);
            model.addAttribute("listofjingdian",listofjingdian);//第（curr - 1）*limit 到 curr*limit-1条结果
            return "search_jingdian";
        }
        else if(type==2)
        {

            model.addAttribute("count", xianlu.size());//结果总数
            model.addAttribute("curr",curr); //当前页码
            List<WebPageData> listofxianlu=new ArrayList<>(); //为了分页
            System.out.println("curr*limit="+curr*limit+" xianlu.size="+xianlu.size());

            int start,end;
            start=curr*limit-limit;
            if(curr*10-1<xianlu.size())
            {
                end=curr*limit;
            }
            else
            {
                end=xianlu.size();
            }


            listofxianlu=xianlu.subList(start,end);


            model.addAttribute("listofxianlu",listofxianlu);//第（curr - 1）*limit 到 curr*limit-1条结果
            return "search_xianlu";

        }
        else if(type==3) //攻略
        {
            model.addAttribute("count", gonglue.size());//结果总数
            model.addAttribute("curr",curr); //当前页码
            List<GongLue_Data> listofgonglue=new ArrayList<>(); //为了分页
            int start,end;
            start=curr*limit-limit;
            if(curr*10-1<gonglue.size())
            {
                end=curr*limit;
            }
            else
            {
                end=gonglue.size();
            }

            for (int i=start;i<end;i++)
            {
                listofgonglue.add((GongLue_Data) gonglue.get(i));
               System.out.println (((GongLue_Data) gonglue.get(i)).getDescription());
                System.out.println (((GongLue_Data) gonglue.get(i)).getPosition());

            }

            model.addAttribute("listofgonglue",listofgonglue);//第（curr - 1）*limit 到 curr*limit-1条结果
            return "search_gonglue";

        }


        model.addAttribute("count",gonglue.size()+xianlu.size());
        model.addAttribute("listofgonglue",gonglue.subList(0,gonglue.size()>4?4:gonglue.size()));
        model.addAttribute("listofxianlu",xianlu.subList(0,xianlu.size()>4?4:xianlu.size()));
        return "search_all";
    }








    @RequestMapping("/detail")
    String detail(@Param("id") String id, @Param("type") Integer type, Model model) {

        //添加用户登录状态
        UserDetails userDetails;
        if(SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal() instanceof UserDetails)
        {
            userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            model.addAttribute("name",userDetails.getUsername());

            model.addAttribute("state",1);

        }
        else
        {
            model.addAttribute("state",0);
        }



        if(type==4)
        {
            return "detail_jingdian";
        }
        if(type==3)
        {
            try {
                Vector<WebPageData> results=searchEngine.search(id,"gonglue",GongLue_Data.class,"title");
                model.addAttribute("gonglue",(GongLue_Data)results.get(0));


            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return "detail_gonglue";
        }
        else if (type==2)
        {

            try {
                Vector<WebPageData> result_temp=searchEngine.search(id,"clh-search-engine",WebPageData.class,"title");
                WebPageData result=result_temp.firstElement();
                model.addAttribute("result",result);

                System.out.println("size= "+result.getBase64PictureCode().size());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //return "detail_xianlu";
            return "detail_xianlu";
        }
        return "detail_gonglue";
    }
    @RequestMapping("/recommend")
    String detail(Model model) {


        UserDetails userDetails;
        if(SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal() instanceof UserDetails)
        {
            userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            model.addAttribute("name",userDetails.getUsername());

            model.addAttribute("state",1);

        }
        else
        {
            model.addAttribute("state",0);
        }


        return "recommend";
    }

/*
    @ResponseBody
    @RequestMapping(value = "/photo", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] testphoto(@Param("id") String id) throws IOException, InterruptedException {

        WebPageData xianlu= searchEngine.search(id,"clh-search-engine","title").firstElement();


       return  Base64.getDecoder().decode(xianlu.getBase64PictureCode().get(0)) ;


    }*/

}