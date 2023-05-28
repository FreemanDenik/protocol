package com.execute.protocol.app;

import com.execute.protocol.auth.models.JwtLoginResponse;
import com.execute.protocol.auth.models.JwtRequest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * Класс обертка для класса {@link TestRestTemplate} <br>
 * Назначение, выполнять запрос с выполненным входом (логин)
 */
public class TestTemplateLogin {
    private TestRestTemplate restTemplate;
    private String token;

    public TestTemplateLogin(TestRestTemplate restTemplate, String email, String password) {
        this.restTemplate = restTemplate;
        JwtRequest testUser = JwtRequest.builder().email(email).password(password).build();
        var urlTemplate = UriComponentsBuilder.fromUriString("/api/auth/login");
        var uri = urlTemplate.encode().toUriString();
        ResponseEntity<JwtLoginResponse> loginResponse = restTemplate.postForEntity(
                uri,
                new HttpEntity<>(testUser, new HttpHeaders()),
                JwtLoginResponse.class);
        token = loginResponse.getBody().getAccessToken();
    }
//    public <T, S> ResponseEntity<T> post(String url, Class<T> clazz, S model){
//        return post(url, new HttpEntity<>(new HttpHeaders()), clazz, model);
//    }
//    public <T> ResponseEntity<T> post(String url, Class<T> clazz){
//        return post(url, new HttpEntity<>(new HttpHeaders()), clazz, String.class);
//    }
    public <T, S> ResponseEntity<S> post(String url, HttpEntity<T> entity, T model, Class<S> clazz) {
        var urlTemplate = UriComponentsBuilder.fromUriString(url);
        var uri = urlTemplate.encode().toUriString();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);
        httpHeaders.putAll(entity.getHeaders());
        HttpEntity<T> entity1 = new HttpEntity<>(model, httpHeaders);
        return restTemplate.exchange(uri, HttpMethod.POST, entity1, clazz);
    }
}
