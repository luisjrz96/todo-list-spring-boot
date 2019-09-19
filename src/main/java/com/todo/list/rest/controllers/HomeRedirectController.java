package com.todo.list.rest.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeRedirectController {

    @GetMapping("/")
    public ModelAndView home(){
        return new ModelAndView(String.format("redirect:%s", "/swagger-ui.html"));

    }
}
