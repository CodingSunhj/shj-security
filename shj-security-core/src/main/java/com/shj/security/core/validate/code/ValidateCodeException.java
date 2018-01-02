package com.shj.security.core.validate.code;


import org.springframework.security.core.AuthenticationException;

public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String explanation) {
        super(explanation);
    }

    public ValidateCodeException(String msg, Throwable t) {
        super(msg, t);
    }
}
