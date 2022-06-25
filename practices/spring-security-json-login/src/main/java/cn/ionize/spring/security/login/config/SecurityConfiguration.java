package cn.ionize.spring.security.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author zhiheng.wang
 * @version 1.0.0
 */
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/system/**")
                .authorizeHttpRequests().anyRequest().authenticated().and()
                .apply(new JsonLoginAuthenticationConfigurer())
                .loginProcessingUrl("/system/login");
        return http.build();
    }
}
