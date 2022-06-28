package cn.ionize.spring.security.login.config;

import cn.ionize.spring.security.login.filter.PreUsernamePasswordAuthenticationFilter;
import cn.ionize.spring.security.login.model.vo.Result;
import cn.ionize.spring.security.login.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author zhiheng.wang
 * @version 1.0.0
 */
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration {

    @Bean
    public UserDetailsService defaultUserDetailService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.builder().username("admin").password("{noop}123456").roles("ADMIN").build());
        return inMemoryUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/system/**")
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests().anyRequest().authenticated().and()
                .addFilterBefore(new PreUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin().loginProcessingUrl("/system/login")
                .successHandler((request, response, authentication) -> {
                    ResponseUtil.toJson(response, Result.success("登录成功"));
                })
                .failureHandler((request, response, exception) -> {
                    Result<Object> failureResult = Result.failure(exception.getMessage());
                    if (exception instanceof CredentialsExpiredException) {
                        failureResult.setMessage("密码已过期, 请联系管理员");
                    } else if (exception instanceof LockedException) {
                        failureResult.setMessage("账号被锁定, 请联系管理员");
                    } else if (exception instanceof DisabledException) {
                        failureResult.setMessage("账号已关闭, 请联系管理员");
                    } else if (exception instanceof BadCredentialsException) {
                        failureResult.setMessage("用户名或者密码错误");
                    }
                    ResponseUtil.toJson(response, failureResult);
                })
                .and()
                .exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
                    Result<Object> failureResult = Result.failure(authException.getMessage());
                    ResponseUtil.toJson(response, failureResult);
                });
        return http.build();
    }
}
