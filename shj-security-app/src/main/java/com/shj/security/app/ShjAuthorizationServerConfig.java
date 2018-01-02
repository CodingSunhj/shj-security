package com.shj.security.app;

import com.shj.security.core.properties.OAuth2ClientProperties;
import com.shj.security.core.properties.OAuth2Properties;
import com.shj.security.core.properties.SecurityProperties;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAuthorizationServer  //认证服务器
public class ShjAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private SecurityProperties securityProperties;
    private TokenStore redisTokenStore;
    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
       endpoints.tokenStore(redisTokenStore)
               .authenticationManager(authenticationManager)
               .userDetailsService(userDetailsService);
       if(jwtAccessTokenConverter!=null && jwtTokenEnhancer!=null){
           TokenEnhancerChain enhancerChain = new TokenEnhancerChain(); //jwt增强器，用于添加自定义的jwt内容
           List<TokenEnhancer> enhancers = new ArrayList<>();
           enhancers.add(jwtTokenEnhancer);
           enhancers.add(jwtAccessTokenConverter);
           enhancerChain.setTokenEnhancers(enhancers);
           endpoints
                   .tokenEnhancer(enhancerChain)
                   .accessTokenConverter(jwtAccessTokenConverter);
       }
    }

    //替换配置中的client-id和client-secret,并配置其它的一些参数
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder inMemoryClientDetailsServiceBuilder =clients.inMemory();
        if(ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())){
            for(OAuth2ClientProperties config : securityProperties.getOauth2().getClients()){
                inMemoryClientDetailsServiceBuilder.withClient(config.getClientId())
                        .secret(config.getClientSecret())
                        .accessTokenValiditySeconds(config.getAccessTokenValidateSeconds())   //令牌有效期是7200秒
                        .authorizedGrantTypes("refresh_token","password")  //接受的模式
                        .scopes("all","read","write");//允许的权限种类
            }
        }
    }
}
