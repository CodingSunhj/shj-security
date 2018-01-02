package com.shj.security.core.validate.code.impl;

import com.shj.security.core.validate.code.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor{
    @Autowired
    ValidateCodeRepository validateCodeRepository;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    //Spring开发技巧，依赖查找，查找所有实现ValidateCodeGenerator的类，并将其放在map中
    @Autowired
    private Map<String,ValidateCodeGenerator> validateCodeGeneratorMap;
    @Override
    public void create(ServletWebRequest request) throws Exception {
        C validateCode = generate(request);
        save(request,validateCode);
        send(request,validateCode);

    }

    private void save(ServletWebRequest request, C validateCode) {
        ValidateCode code = new ValidateCode(validateCode.getCode(),validateCode.getExpirTime());
        validateCodeRepository.save(request,code,getValidateCodeType(request));
    }

    protected abstract void send(ServletWebRequest request, C validateCode) throws ServletRequestBindingException, IOException;

    @SuppressWarnings("unchecked")
    private C generate(ServletWebRequest request) {
        String type = getProcessorType(request).toLowerCase();
        ValidateCodeGenerator validateCodeGenerator = validateCodeGeneratorMap.get(type+ValidateCodeGenerator.class.getSimpleName());
        if (validateCodeGenerator == null) {
            throw new ValidateCodeException("验证码生成器" + (type+ValidateCodeGenerator.class.getSimpleName()) + "不存在");
        }
        return (C) validateCodeGenerator.generate(request.getRequest());
    }

    private String getProcessorType(ServletWebRequest request) {
        return StringUtils.substringAfter(request.getRequest().getRequestURI(),"/code/");//根据url /code/sms或者/code/image 获得需要获取的类型
    }
    /**
     * 根据请求的url获取校验码的类型
     *
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }
    /**
     * 构建验证码放入session时的key
     *
     * @param request
     * @return
     */
    private String getSessionKey(ServletWebRequest request) {
        return SESSION_KEY_PREFIX + getValidateCodeType(request).toString().toUpperCase();
    }


    @Override
    public void validate(ServletWebRequest request) {

        ValidateCodeType processorType = getValidateCodeType(request);
        String sessionKey = getSessionKey(request);

        C codeInSession = (C) validateCodeRepository.get(request,processorType);

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                    processorType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException(processorType + "验证码不存在");
        }

        if (codeInSession.isExpired()) {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCodeException(processorType + "验证码已过期");
        }

        if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码不匹配");
        }

        validateCodeRepository.remove(request,processorType);
    }
}
