package com.searcher.searcher.Controller;

import com.searcher.searcher.Domain.SysUser;
import com.searcher.searcher.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@EnableAutoConfiguration
public class loginController {
    @Autowired
    UserService userService;



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
    String signup(Model model,@Param("error") Integer error) {
        UserDetails user=new SysUser();
        model.addAttribute("user",user);
        if(error==null)
            model.addAttribute("error",false);

        else
            model.addAttribute("error",true  );

        return "signup";
    }

    @RequestMapping("adduser")
    String adduser(Model model,SysUser user)
    {


        if(userService.existUser(user.getUsername()))
        {
            return "redirect:/signup?error=1";

        }

        userService.addUser(user);

        return "redirect:/login";


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