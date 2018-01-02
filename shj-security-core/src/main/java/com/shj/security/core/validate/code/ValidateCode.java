package com.shj.security.core.validate.code;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ValidateCode implements Serializable{
    private String code;
    private LocalDateTime expireTime;

    public ValidateCode(String code, int expireTime) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }
    public ValidateCode(String code, LocalDateTime expireTime){
        this.code = code;
        this.expireTime = expireTime;
    }


    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expireTime);
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpirTime() {
        return expireTime;
    }

    public void setExpirTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
