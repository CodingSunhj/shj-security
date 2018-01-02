package com.shj.security.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

public class ShjSpringSocialConfigurer extends SpringSocialConfigurer {
    private String filterProcessesUrl;
    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    public ShjSpringSocialConfigurer(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }



    @SuppressWarnings("unchecked")
    @Override
    protected <T> T postProcess(T object) {
        //放在过滤器列上的filter，改变社会化登陆中的路径，默认为/auth处理
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
        filter.setFilterProcessesUrl(filterProcessesUrl);
        // 判断是否存在社交认证后处理器（即判断是浏览器端还是app端）
        if (socialAuthenticationFilterPostProcessor != null) {
            socialAuthenticationFilterPostProcessor.process(filter);
        }
        return (T) filter;
    }
    public SocialAuthenticationFilterPostProcessor getSocialAuthenticationFilterPostProcessor() {
        return socialAuthenticationFilterPostProcessor;
    }

    public void setSocialAuthenticationFilterPostProcessor(SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor) {
        this.socialAuthenticationFilterPostProcessor = socialAuthenticationFilterPostProcessor;
    }
}
