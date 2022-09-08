package com.execute.protocol.auth.controllers;

import com.execute.protocol.auth.enums.EnumProviders;
import com.execute.protocol.auth.models.Vkontakte;
import com.execute.protocol.auth.services.OtherOAuth2ProviderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    final
    OtherOAuth2ProviderService otherOAuth2ProviderService;
    final
    Vkontakte other;
    public HomeController(OtherOAuth2ProviderService otherOAuth2ProviderService, Vkontakte other) {
        this.otherOAuth2ProviderService = otherOAuth2ProviderService;
        this.other = other;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
}
