package com.shj.security.browser.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shj.security.core.properties.BrowserProperties;
import com.shj.security.core.properties.LoginType;
import com.shj.security.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("shjAuthenticationSuccessHandler")
public class ShjAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
    private Logger logger = LoggerFactory.getLogger(ShjAuthenticationSuccessHandler.class);
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityProperties securityProperties;
    private RequestCache requestCache = new HttpSessionRequestCache();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(securityProperties.getBrowser().getLoginType()== LoginType.JSON){
            logger.info("成功");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(authentication));
        }else{
            // 如果设置了earthchen.security.browser.singInSuccessUrl，总是跳到设置的地址上
            // 如果没设置，则尝试跳转到登录之前访问的地址上，如果登录前访问地址为空，则跳到网站根路径上
            if (StringUtils.isNotBlank(securityProperties.getBrowser().getSingInSuccessUrl())) {
                requestCache.removeRequest(request, response);
                setAlwaysUseDefaultTargetUrl(true);
                setDefaultTargetUrl(securityProperties.getBrowser().getSingInSuccessUrl());
            }
            super.onAuthenticationSuccess(request,response,authentication);
        }

    }
}
