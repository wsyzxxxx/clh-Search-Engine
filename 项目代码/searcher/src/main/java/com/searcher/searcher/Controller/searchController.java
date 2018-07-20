package com.searcher.searcher.Controller;

import com.searcher.searcher.DataFormat.WebPageData;
import com.searcher.searcher.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
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
        Vector<WebPageData> xianlu= searchEngine.search(keyword,"clh-search-engine","title","tags");
        xianlu= searchEngine.search(keyword,"clh-search-engine","title","tags");



        List<String> gonglue=new ArrayList<>();
        for(int i=41;i<99;i++)
        {
            jingdian.add(String.valueOf(i));
            gonglue.add(String.valueOf(i));
        }

        System.out.println("size= "+xianlu.size());

        if(type==4)//搜索景点
        {
            model.addAttribute("count",jingdian.size());
            model.addAttribute("count", jingdian.size());//结果总数
            model.addAttribute("curr",curr); //当前页码
            List<String> listofjingdian=new ArrayList<>(); //为了分页
            if(curr*10-1<jingdian.size())
            {
                listofjingdian=jingdian.subList(curr*limit-limit,curr*limit);
            }
            else
            {
                listofjingdian=jingdian.subList(curr*limit-limit,jingdian.size());

            }

            model.addAttribute("listofjingdian",listofjingdian);//第（curr - 1）*limit 到 curr*limit-1条结果
            return "search_jingdian";
        }
        else if(type==2)
        {

            model.addAttribute("count", xianlu.size());//结果总数
            model.addAttribute("curr",curr); //当前页码

            List<WebPageData> listofxianlu=new ArrayList<>(); //为了分页

            System.out.println("curr*limit="+curr*limit+" xianlu.size="+xianlu.size());

            if(curr*limit<=xianlu.size())
            {
                System.out.println("cur="+curr);
                listofxianlu=xianlu.subList(curr*limit-limit,curr*limit);
            }
            else
            {
                System.out.println("end page");
                listofxianlu=xianlu.subList(curr*limit-limit,xianlu.size());

            }

            model.addAttribute("listofxianlu",listofxianlu);//第（curr - 1）*limit 到 curr*limit-1条结果
            return "search_xianlu";

        }
        else if(type==3)
        {
            model.addAttribute("count", gonglue.size());//结果总数
            model.addAttribute("curr",curr); //当前页码
            List<String> listofgonglue=new ArrayList<>(); //为了分页
            if(curr*10-1<gonglue.size())
            {
                listofgonglue=gonglue.subList(curr*limit-limit,curr*limit);
            }
            else
            {
                listofgonglue=jingdian.subList(curr*limit-limit,gonglue.size());

            }
            System.out.println(listofgonglue);

            model.addAttribute("listofgonglue",listofgonglue);//第（curr - 1）*limit 到 curr*limit-1条结果
            return "search_gonglue";
        }

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
            return "detail_gonglue";
        }
        else if (type==2)
        {

            try {
                Vector<WebPageData> result_temp=searchEngine.search(id,"clh-search-engine","title");
                WebPageData result=result_temp.firstElement();
                model.addAttribute("result",result);



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




}