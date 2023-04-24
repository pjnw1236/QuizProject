package com.mysite.sbb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/sbb")
    public String index() {
        return "index";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/question/list";
    }
}
