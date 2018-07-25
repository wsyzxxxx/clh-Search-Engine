package com.searcher.searcher.Controller;


import org.omg.CORBA.INTERNAL;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;

@Controller
@EnableAutoConfiguration

public class update {

    @RequestMapping("update")
    @ResponseBody
    public String updateTitle_Id(@Param("option") Integer option){

        return "";
    }
}
