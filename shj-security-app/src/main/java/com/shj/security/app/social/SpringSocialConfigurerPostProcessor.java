package com.shj.security.app.social;


import com.shj.security.core.properties.SecurityConstants;
import com.shj.security.core.social.ShjSpringSocialConfigurer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {

    //所有的bean初始化之前
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    //所有的bean初始化之后
    //此处是在APP环境下，将注册地址变成下面所示
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (StringUtils.equals(beanName, "shjSocialSecurityConfigure")) {
            ShjSpringSocialConfigurer config = (ShjSpringSocialConfigurer) bean;
            config.signupUrl("/social/signUp");
            return config;
        }
        return bean;
    }

}
