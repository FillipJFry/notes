package com.goit.letscode.notes.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

// TODO: implement

//@Controller
//@RequestMapping("/")
public class AuthController {

    //@GetMapping("/login")
    public ModelAndView gotoLogin() {

        return new ModelAndView("auth/login");
    }

    //@GetMapping("/register")
    public ModelAndView register() {

        return new ModelAndView("auth/register");
    }
}
