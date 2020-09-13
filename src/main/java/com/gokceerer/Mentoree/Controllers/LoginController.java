package com.gokceerer.Mentoree.Controllers;

import com.gokceerer.Mentoree.Models.SQL.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {
    @GetMapping(value = {"/", "/login"})
    public ModelAndView showLogin(){
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("user",new User());
        return mav;
    }
}
