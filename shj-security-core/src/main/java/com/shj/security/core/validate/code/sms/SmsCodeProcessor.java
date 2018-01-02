package com.shj.security.core.validate.code.sms;

import com.shj.security.core.validate.code.ValidateCode;
import com.shj.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletRequest;

@Component("smsValidateCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {
    @Autowired
    private SmsCodeSender smsCodeSender;
    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws ServletRequestBindingException {
        String mobile = ServletRequestUtils.getRequiredStringParameter((ServletRequest) request.getRequest(),"mobile");
        smsCodeSender.send(mobile,validateCode.getCode());
    }
}
