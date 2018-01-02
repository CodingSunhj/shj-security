package com.shj.security.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.shj.security.core.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.*;

import com.shj.security.dto.User;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/user")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private ProviderSignInUtils providerSignInUtils;
	@Autowired
	private SecurityProperties securityProperties;

	@RequestMapping(value="/user",method=RequestMethod.GET)
	public List<User> query(Pageable page){
		List<User> users = new ArrayList<>();
		User user = new User();
		users.add(user);
		users.add(user);
		users.add(user);
		return users;
	}
	@GetMapping("/me")
//	public Object getCurrentUser(@AuthenticationPrincipal UserDetails user) {
	public Object getCurrentUser(Authentication  user,HttpServletRequest request) throws UnsupportedEncodingException {
		String header = request.getHeader("Authorization");
		String token = StringUtils.substringAfter(header,"bearer ");
		Claims claims = Jwts.parser().setSigningKey(securityProperties.getOauth2().getJwtSigningKey().getBytes("utf-8"))
				.parseClaimsJws(token).getBody();
		String company = (String) claims.get("company");
		logger.info(company);
		return user;
	}

	@PostMapping("/regist")
	public void regist(User user, HttpServletRequest servletRequest){
		//注册用户
		String userId = user.getUsername();
		providerSignInUtils.doPostSignUp(userId,new ServletWebRequest(servletRequest));
	}
}
