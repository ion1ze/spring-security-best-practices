# Spring Security Multi User Detail Service

在设计我们的系统时候，往往不止一种用户体系，通常管理员一套用户体系，会员用户一套用户体系，这样的好处是可以起到隔离作用，一些功能可以分开实现。

在 `Spring Security` 配置类中，我们可以创建俩套 `SecurityFilterChain`，通过 `AntMatcher` 来隔离不同的请求。比如管理员用户的通过 `/system/**`
通过这个路径下的请求，会员用户通过 `/member/**` 通过这个路径下的请求。

这里需要注意的是由于需要一个默认的 `UserDetailService` 注册到 `Spring IOC` 容器中兜底，不然 `Spring Security` 会启用一个默认的 `InMemoryUserDetailsMannager`
产生一个随机用户。这里我们直接注册一个不做实现的 `UserDetailService`。然后分别创建俩个 `UserDetailService` 的接口，然后进行实现，这里我创建了 `SystemUserDetailService`
和 `MemberUserDetailService` 这俩个接口。

```java

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
```