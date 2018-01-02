package com.shj.security.core.validate.code;

import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;

public interface ValidateCodeGenerator {
    ValidateCode generate(ServletRequest request);
}
