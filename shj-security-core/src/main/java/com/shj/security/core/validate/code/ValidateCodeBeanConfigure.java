package com.shj.security.core.validate.code;

import com.shj.security.core.properties.SecurityProperties;
import com.shj.security.core.validate.code.image.ImageCodeGenerator;
import com.shj.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.shj.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidateCodeBeanConfigure {
    @Autowired
    private SecurityProperties securityProperties;
    @Bean
    @ConditionalOnMissingBean(name = "imageValidateCodeGenerator")  //当不存在这样的bean的时候使用这个bean
    public ValidateCodeGenerator imageValidateCodeGenerator(){
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setSecurityProperties(securityProperties);
        return codeGenerator;
    }

    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)  //如果系统中存在SmsCodeSender的实现类，则不会调用下述默认
    public SmsCodeSender smsCodeGenerator(){
        return new DefaultSmsCodeSender();
    }
}
