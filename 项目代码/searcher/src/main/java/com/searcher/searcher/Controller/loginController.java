package com.searcher.searcher.Controller;

import com.searcher.searcher.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@EnableAutoConfiguration
public class loginController {


    @RequestMapping("/login")
    String signin(Model model ,@Param("error") Integer error) {

        if(error==null)
            return "login";

        if(error==1)
            model.addAttribute("error",true);
        else
            model.addAttribute("error",false);
        return "login";
    }
    @RequestMapping("/")
    String home(Model model) {
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

        return "home";
    }
    @RequestMapping("/signup")
    String signup(Model model) {

        return "signup";
    }

    @RequestMapping("/logout")
    String logout(Model model, HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        model.addAttribute("state",0);

        return "home";
    }

}