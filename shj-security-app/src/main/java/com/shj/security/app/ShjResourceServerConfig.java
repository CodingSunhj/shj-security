package com.shj.security.app;

import com.shj.security.app.authentication.openid.OpenIdAuthenticationSecurityConfig;
import com.shj.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.shj.security.core.properties.SecurityConstants;
import com.shj.security.core.properties.SecurityProperties;
import com.shj.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableResourceServer
public class ShjResourceServerConfig extends ResourceServerConfigurerAdapter{

    @Autowired
    protected AuthenticationSuccessHandler shjAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler shjAuthenticationFailureHandler;

    @Autowired
    SecurityProperties securityProperties;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private SpringSocialConfigurer shjSocialSecurityConfigure;
    @Autowired
    DataSource dataSource;
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;
    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(shjAuthenticationSuccessHandler)
                .failureHandler(shjAuthenticationFailureHandler);
        http
                .apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(shjSocialSecurityConfigure)
                .and()
                .apply(openIdAuthenticationSecurityConfig)
                .and()
                .authorizeRequests()
                .antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+"/*",
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        securityProperties.getBrowser().getLoginPage(),
                        securityProperties.getBrowser().getLogoutPage(),
                        SecurityConstants.DEFAULT_SESSION_INVALID_URL).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();  //禁用csrf防护
    }
}
