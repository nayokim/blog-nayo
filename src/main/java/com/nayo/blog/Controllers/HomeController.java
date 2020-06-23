package com.nayo.blog.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Path;

@Controller
public class HomeController {

    @GetMapping("/")
    public String introduction(){
        return "home/index";
    }

    @GetMapping("/{name}")
    public String introductionName(@PathVariable String name, Model model){
        model.addAttribute("userName", name);
        return "home/index";
    }

    @GetMapping("/aboutme")
    public String intro (Model model){
        return "home/aboutme";
    }



}
