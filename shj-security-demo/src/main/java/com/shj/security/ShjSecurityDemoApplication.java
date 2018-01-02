package com.shj.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ShjSecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShjSecurityDemoApplication.class, args);
	}
	
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
}
