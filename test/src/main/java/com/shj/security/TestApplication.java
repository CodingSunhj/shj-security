package com.shj.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController
public class TestApplication {

    @Autowired
    private ProviderSignInUtils providerSignInUtils;


    @GetMapping("/me")
    public Authentication me(Authentication user) {
        return user;
    }

    @PostMapping("/user/regist")
    public void regist(HttpServletRequest request) {
        providerSignInUtils.doPostSignUp("test", new ServletWebRequest(request));
    }


    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}

