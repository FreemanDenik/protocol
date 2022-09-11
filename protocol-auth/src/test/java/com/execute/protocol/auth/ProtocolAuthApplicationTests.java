package com.execute.protocol.auth;


import com.execute.protocol.auth.configs.WebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.execute.protocol.auth.services.UserDetailsServiceImpl;

@SpringBootTest
class ProtocolAuthApplicationTests  {
    @Test
    public void Test() {
//        RestTemplate restTemplate = new RestTemplate();
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
//        ResponseEntity<String>  result = restTemplate.getForEntity("https://login.yandex.ru/info?format=json&jwt_secret=imdenikandiwantmanymanymoney&with_openid_identity=1", String.class, httpEntity);


    }
}
