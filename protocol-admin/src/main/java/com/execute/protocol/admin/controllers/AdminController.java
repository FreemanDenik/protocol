package com.execute.protocol.admin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/")
    public String index(){
        return "admin/index";
    }
    @GetMapping("/save")
    @ResponseBody
    public String insert(){
        return "yes";
    }

}
