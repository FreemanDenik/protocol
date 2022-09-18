package com.execute.protocol.admin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller


public class AdminController {
    @GetMapping("/admin")
    public String index(){
        return "admin/index";
    }
}
