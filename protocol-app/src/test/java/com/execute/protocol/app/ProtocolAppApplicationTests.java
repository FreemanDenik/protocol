package com.execute.protocol.app;

import com.execute.protocol.auth.models.AbstractProvider;
import com.execute.protocol.auth.models.Vkontakte;
import com.execute.protocol.auth.models.Yandex;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@SpringBootTest
class ProtocolAppApplicationTests {

    @Autowired
    Yandex yandex ;
    @Test
    void contextLoads() {

//        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
//        restTemplateBuilder.configure(restTemplate);
//        TestRestTemplate testRestTemplate = new TestRestTemplate(restTemplateBuilder);
//
//        String email = "test@mail.ru";
//        String token = "testtoken";
//
//        restTemplate.getForObject("https://oauth.yandex.ru/verification_code#access_token="+token+"&expires_in=1000", String.class);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("cookie", "OAuth " + token);
//        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
//        ResponseEntity<String> result = restTemplate.getForEntity("https://login.yandex.ru/info?format=json&jwt_secret=imdenikandiwantmanymanymoney&with_openid_identity=1", String.class, httpEntity);

        var tt = test23(Optional.ofNullable(yandex));
    }
    public String test23(Optional<? extends AbstractProvider> provider) {
        if (provider.get() instanceof Yandex){
            return "y";
        }else if (provider.get() instanceof Vkontakte) {
            return "V";
        }
        return null;
    }

}
