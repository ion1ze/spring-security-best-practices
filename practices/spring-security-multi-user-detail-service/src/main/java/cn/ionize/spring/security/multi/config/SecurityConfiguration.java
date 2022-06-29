package cn.ionize.spring.security.multi.config;

import cn.ionize.spring.security.multi.filter.PreUsernamePasswordAuthenticationFilter;
import cn.ionize.spring.security.multi.model.entity.MemberUser;
import cn.ionize.spring.security.multi.model.entity.SystemUser;
import cn.ionize.spring.security.multi.model.vo.Result;
import cn.ionize.spring.security.multi.service.MemberUserService;
import cn.ionize.spring.security.multi.service.SystemUserService;
import cn.ionize.spring.security.multi.util.ResponseUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author zhiheng.wang
 * @version 1.0.0
 */
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String DEFAULT_SYSTEM_ANT_MATCHER = "/system/**";
    private static final String DEFAULT_MEMBER_ANT_MATCHER = "/member/**";
    private static final String DEFAULT_SYSTEM_LOGIN_URL = "/system/login";
    private static final String DEFAULT_MEMBER_LOGIN_URL = "/member/login";

    @Bean
    public UserDetailsService defaultUserDetailService() {
        return username -> {
            throw new UsernameNotFoundException("用户名或者密码不正确");
        };
    }

    @Bean
    public SecurityFilterChain systemFilterChain(HttpSecurity http, SystemUserService systemUserService) throws Exception {
        http.antMatcher(DEFAULT_SYSTEM_ANT_MATCHER).csrf().disable().cors().and()
                .addFilterBefore(new PreUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(systemUserService::loadUserByUsername)
                .formLogin().loginProcessingUrl(DEFAULT_SYSTEM_LOGIN_URL)
                .successHandler((request, response, authentication) -> {
                    SystemUser user = (SystemUser) authentication.getPrincipal();
                    // 密码脱敏处理
                    user.setPassword(null);
                    ResponseUtil.toJson(response, Result.success("登录成功", user));
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
                }).and()
                .exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
                    Result<Object> failureResult = Result.failure(authException.getMessage());
                    ResponseUtil.toJson(response, failureResult);
                });
        return http.build();
    }


    @Bean
    public SecurityFilterChain memberFilterChain(HttpSecurity http, MemberUserService memberUserService) throws Exception {
        http.antMatcher(DEFAULT_MEMBER_ANT_MATCHER).csrf().disable().cors().and()
                .authorizeHttpRequests().anyRequest().authenticated().and()
                .addFilterBefore(new PreUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(memberUserService::loadUserByUsername)
                .formLogin().loginProcessingUrl(DEFAULT_MEMBER_LOGIN_URL)
                .successHandler((request, response, authentication) -> {
                    MemberUser user = (MemberUser) authentication.getPrincipal();
                    // 密码脱敏处理
                    user.setPassword(null);
                    ResponseUtil.toJson(response, Result.success("登录成功", user));
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
                }).and()
                .exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
                    Result<Object> failureResult = Result.failure(authException.getMessage());
                    ResponseUtil.toJson(response, failureResult);
                });
        return http.build();
    }
}
