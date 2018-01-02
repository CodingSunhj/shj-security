package com.shj.security.browser.logout;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shj.security.core.support.SimpleResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
public class ShjLogoutSuccessHandler implements LogoutSuccessHandler {

    /**
     * 默认的退出成功处理器，如果设置了shj.security.browser.signOutUrl，则跳到配置的地址上，x
     * 如果没配置，则返回json格式的响应。
     */

        private Logger logger = LoggerFactory.getLogger(getClass());

        public ShjLogoutSuccessHandler(String signOutSuccessUrl) {
            this.signOutSuccessUrl = signOutSuccessUrl;
        }

        private String signOutSuccessUrl;

        private ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                throws IOException, ServletException {

            logger.info("退出成功");

            if (StringUtils.isBlank(signOutSuccessUrl)) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse("退出成功")));
            } else {
                response.sendRedirect(signOutSuccessUrl);
            }

        }

}
