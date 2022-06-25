package cn.ionize.spring.security.login.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * @author zhiheng.wang
 * @version 1.0.0
 */
public class JsonLoginAuthenticationConfigurer extends AbstractHttpConfigurer<JsonLoginAuthenticationConfigurer, HttpSecurity> {

    private String loginProcessingUrl;

    @Override
    public void init(HttpSecurity http) throws Exception {

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

    }

    public JsonLoginAuthenticationConfigurer loginProcessingUrl(String loginProcessingUrl){
        this.loginProcessingUrl = loginProcessingUrl;
        return this;
    }

    public String getLoginProcessingUrl() {
        return loginProcessingUrl;
    }
}
