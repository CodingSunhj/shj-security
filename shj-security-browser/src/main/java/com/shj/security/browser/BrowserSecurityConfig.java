package com.shj.security.browser;

import com.shj.security.browser.session.ShjExpiredSessionStrategy;
import com.shj.security.browser.session.ShjInvalidSessionStrategy;
import com.shj.security.core.authentication.AbstractChannelSecurityConfig;
import com.shj.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.shj.security.core.authorize.AuthorizeConfigManager;
import com.shj.security.core.properties.SecurityConstants;
import com.shj.security.core.properties.SecurityProperties;
import com.shj.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.*;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    SecurityProperties securityProperties;
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private SpringSocialConfigurer shjSocialSecurityConfigure;
    @Autowired
    DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
//        tokenRepository.setCreateTableOnStartup(true);//启动项目自动创建表
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        applyPasswordAuthenticationConfig(http);

        http
               .apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(shjSocialSecurityConfigure)
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)
                .and()
                .sessionManagement()
                .invalidSessionStrategy(invalidSessionStrategy)  //session过期
                .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
                .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())    //当session数量达到最大的数量时候，阻止后边的登陆
                .expiredSessionStrategy(sessionInformationExpiredStrategy)  //session并发，唯一登陆时，后边登陆顶掉前边登陆，可以在这里记录一些信息
                .and()
                .and()
                .logout()
                .logoutUrl("/signOut")  //执行退出登录的地址
                .logoutSuccessUrl("/logout-success.html")   //退出登录成功的地址
                .deleteCookies("JSESSIONID")    //删除cookie
              //  .logoutSuccessHandler(实现LogoutSuccessHandler接口)     //与logoutSuccessUrl互斥，其中一个接管
                .and()
                .authorizeRequests()
                .and()
                .csrf().disable();  //禁用csrf防护
        authorizeConfigManager.config(http.authorizeRequests());
    }
}
