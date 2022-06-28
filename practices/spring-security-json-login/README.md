# Spring Security JSON Login

在整合 `Spring Security` 时, `Spring Security` 默认给我们提供了一个 `FormLogin`
的接口，但是现在大多数的项目都是前后端分离的项目，一般而言，登录的接口和其他接口约定一致，都是使用 `JSON` 来传递参数。例如：

``` json 
{
    "username":"username",
    "password":"password"
}
```

下面代码是 `UsernamePasswordAuthenticationFilter` 中的关键代码：

```java
public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = obtainUsername(request); // 这边是通过 request.getParameter("username") 获取到 form-data 中的用户名的
        username = (username != null) ? username.trim() : "";
        String password = obtainPassword(request); // 与上面获取用户名的原理类似 request.getParameter("password")
        password = (password != null) ? password : "";
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
                password);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
```

知道了原理就好办了，要解析 `JSON` 数据可以在 `UsernamePasswordAuthenticationFilter` 这个过滤器前面添加一个过滤器，这个过滤器用来解析 `JSON`
数据，然后把解析出来的数据放入 `request` 中，传给 `UsernamePasswordAuthenticationFilter` 进行进一步处理。

具体实现请参考源码中 `PreUsernamePasswordAuthenticationFilter`。

最后是 `Spring Security` 配置。配置好 `FormLogin` 的相关参数、成功和失败处理逻辑，这里也同样返回 `JSON` 数据。：

```java

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
```