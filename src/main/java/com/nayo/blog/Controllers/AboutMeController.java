package com.nayo.blog.Controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class AboutMeController {


    @GetMapping("/aboutme")
    public String intro(){


        return "intro/about";
    }
}
